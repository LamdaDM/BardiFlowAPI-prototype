package io.git.lamdadm.services;

import io.git.lamdadm.data.models.Profile;
import io.git.lamdadm.data.models.Transformer;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

@RequestScope
public class BalanceProjectingService {

    private final Logger logger = LoggerFactory
            .getLogger(BalanceProjectingService.class);

    private final Long globalStart = LocalDate.now().toEpochDay();

    private Double globalBalance = 0.00d;
    private Double globalInterest = 0.00d;
    private Long balanceLastUpdated = globalStart;

    private class InterestBound {
        String name;
        Double amount;
        Double interest;
        Double payment;
        Long lastUpdated;

        public InterestBound(Double amount, Double interest, Double payment) {
            this.amount = amount;
            this.interest = interest;
            this.payment = payment;
            this.lastUpdated = globalStart;
        }

        public InterestBound(String name, Double amount, Double interest, Double payment) {
            this.name = name;
            this.amount = amount;
            this.interest = interest;
            this.payment = payment;
            this.lastUpdated = globalStart;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("InterestBound{");
            sb.append("amount=").append(amount);
            sb.append(", interest=").append(interest);
            sb.append(", payment=").append(payment);
            sb.append(", lastUpdated=").append(lastUpdated);
            sb.append('}');
            return sb.toString();
        }
    }

    private class Event implements Comparable<Event> {

        InterestBound interestBound;
        Long time;
        
        public Event(InterestBound interestBound, Long time) {
            this.interestBound = interestBound;
            this.time = time;
        }

        @Override
        public int compareTo(@NotNull BalanceProjectingService.Event other) {
            return this.time.equals(other.time)
                ? 0
                : this.time > other.time
                    ? 1
                    : -1;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Event{");
            sb.append("interestBound=").append(interestBound);
            sb.append(", time=").append(time);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class EventList extends Vector<Event> {

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Events {");
            listIterator()
                .forEachRemaining(
                    events -> sb.append(events.interestBound.name)
                        .append(": ")
                        .append(events.time.toString())
                        .append(", ")
                );
            sb.append('}');
            return sb.toString();
        }
    }

    private class EventGroup{

        InterestBound interestBound;
        Vector<Long> events;

        public EventGroup(Transformer transformer, Vector<Long> events) {
            this.interestBound = new InterestBound(
                transformer.name,
                transformer.amount,
                transformer.interest,
                transformer.payment
            );
            this.events = events;
        }
    }

    private Single<EventGroup> createEventGroupFromTransformerSingle(Transformer transformer, Long targetDate) {

        return Single.create(emitter -> {

            var events = new Vector<Long>();

            long diffToTarget = calcDifferenceInDays(
                globalStart,
                targetDate
            );
            long diffToReferenceDate = calcDifferenceInDays(
                globalStart,
                transformer.referenceDate.toEpochDay()
            );

            if(transformer.cycle <= diffToTarget) {

                long initialEvent = diffToTarget % transformer.cycle;
//                events.add(initialEvent);

                long curr;
                for(int i = 1; i < calcLengthOfProjection(diffToTarget, transformer.cycle); i++){
                    curr = initialEvent + (long) transformer.cycle * i;
                    logger.debug("Current event: {}, added?: {}", curr, curr <= diffToReferenceDate);
                    if (curr <= diffToTarget && curr <= diffToReferenceDate)
                        events.add(curr);
                    else break;
                }
            }

            var out = new EventGroup(transformer, events);

            logger.debug("With: diffToTarget {}, cycle: {}, length of projection: {}; created EventGroup: {}",
                diffToTarget, transformer.cycle, calcLengthOfProjection(diffToTarget, transformer.cycle), out );
            emitter.onSuccess(out);
        });
    }

    private Flowable<Event> createEventsFromEventGroups(List<EventGroup> eventGroups) {

        return Flowable.create(emitter -> {

            for(EventGroup group: eventGroups) {
                for (Long event: group.events) {
                    var newEvent = new Event(group.interestBound, event);
                    logger.debug("New event: {}", newEvent);
                    emitter.onNext(newEvent);
                }
            }

            emitter.onComplete();

        }, BackpressureStrategy.BUFFER);
    }

    private Single<Double> calcBalance(Vector<Event> events) {

        return Single.create(emitter -> {

            var dbgmsg = "BALANCE: before=" + globalBalance.toString();
            events.forEach(this::consumeEvent);

            logger.debug("{} after={}", dbgmsg, globalBalance.toString());
            emitter.onSuccess(globalBalance);
        });
    }

    private void consumeEvent(Event event) {

        updateAmounts(event.interestBound, event.time);

        if(event.interestBound.payment < 0) makePayment(event);
        else globalBalance += event.interestBound.payment;
    }

    private void makePayment(Event event) {
        if(event.interestBound.payment > event.interestBound.amount) {
            globalBalance += event.interestBound.amount;
            event.interestBound.payment = 0.00d;
        } else {
            globalBalance += event.interestBound.payment;
            event.interestBound.amount += event.interestBound.payment;
        }
    }

    private void updateAmounts(InterestBound interestBound, Long currentDay) {

        logger.debug("Updating global balance {} with time range from {} to {}, current IB: {}",
            globalBalance, balanceLastUpdated, currentDay, interestBound);
        globalBalance = calcGrowth(
            globalBalance,
            globalInterest,
            calcDifferenceInDays(balanceLastUpdated, currentDay)
        );
        balanceLastUpdated = currentDay;

        interestBound.amount = calcGrowth(
            interestBound.amount,
            interestBound.interest,
            calcDifferenceInDays(interestBound.lastUpdated, currentDay)
        );
        interestBound.lastUpdated = currentDay;
    }

    private Flowable<EventGroup> consumeProfile(Profile profile, Long targetDate) {

        return Flowable.create(emitter -> {

            logger.debug("Consuming profile: {}, from {} to {}", profile, globalStart, targetDate);

            globalBalance = profile.finance.balance;
            globalInterest = profile.finance.interest;

            for (Transformer transformer: profile.finance.transformers) {

                emitter.onNext(
                    createEventGroupFromTransformerSingle(transformer, targetDate)
                    .blockingGet()
                );
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    private Long calcLengthOfProjection(Long differenceInDays, Integer cycle) {
        if(differenceInDays == 0) return (long) 1;
        else return
            ((differenceInDays
                - ( differenceInDays % cycle ))
            / cycle)
            + 1;
    }

    private Double calcGrowth(Double initialAmount, Double interest, Long differenceInDays) {
        if(differenceInDays == 0) return initialAmount;
        else return
            initialAmount
            + ((initialAmount
                * (interest / 365))
            / differenceInDays);
    }

    private Long calcDifferenceInDays(Long start, Long end) { return end - start; }

    public Single<Double> projectBalance(Profile profile, Long targetDate) {
        return consumeProfile(profile, targetDate)
            .collectInto(new Vector<EventGroup>(), Vector::add)
            .flatMap(eventGroups -> createEventsFromEventGroups(eventGroups)
                .collectInto(new EventList(), EventList::add)
            ).flatMap(events -> {
                events.sort(Comparator.naturalOrder());
                logger.debug("Sorted events: {}", events);
                return calcBalance(events);
            });
    }

    public Single<Double> projectBalance(Profile profile, LocalDate targetDate) {
        return projectBalance(profile, targetDate.toEpochDay());
    }
}
