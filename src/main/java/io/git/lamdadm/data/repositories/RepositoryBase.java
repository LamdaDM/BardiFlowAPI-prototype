package io.git.lamdadm.data.repositories;

import io.git.lamdadm.data.models.Finance;
import io.git.lamdadm.data.models.Profile;
import io.git.lamdadm.data.models.Transformer;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowIterator;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

public class RepositoryBase {

    protected Single<Profile> profileFullFromRowSet(RowIterator<Row> iterator) throws Exception {
        
        if(iterator.hasNext()) {

            var init = iterator.next();

            return financeFullFromRowSet(init, iterator)
                .map(finance -> new Profile(
                    init.getLong("profile_id"),
                    init.getString("name"),
                    init.getString("email"),
                    init.getString("password"),
                    finance
                ));

        } else { throw new Exception("No row found."); }
    }

    protected Flowable<Transformer> transformerFromRowSet(RowIterator<Row> iterator) throws Exception {

        if(iterator.hasNext()) {

            var init = iterator.next();

            return transformerFromRowSet(init, iterator);
        } else { throw new Exception("No row found."); }
    }

    protected Transformer transformerFromRow(Row row) {
        return new Transformer(
                row.getLong("transformer_id"),
                row.getString("name"),
                row.getString("description"),
                row.getDouble("interest"),
                row.getDouble("amount"),
                row.getDouble("payment"),
                row.getInteger("cycle"),
                row.getLocalDate("reference_date")
        );
    }

    protected Transformer transformerFromRow(AtomicReference<Row> row) {

        return new Transformer(
            row.get().getLong("transformer_id"),
            row.get().getString("name"),
            row.get().getString("description"),
            row.get().getDouble("interest"),
            row.get().getDouble("amount"),
            row.get().getDouble("payment"),
            row.get().getInteger("cycle"),
            row.get().getLocalDate("reference_date")
        );

    }

    protected Flowable<Transformer> transformerFromRowSet(Row init, RowIterator<Row> iterator) {

        return Flowable.create(emitter -> {

            var row = new AtomicReference<>(init);

            emitter.onNext(transformerFromRow(row));

            //TODO: Test if duplicate initially occurs
            iterator.forEachRemaining(it -> emitter
                .onNext(transformerFromRow(it)));

            emitter.onComplete();

        }, BackpressureStrategy.BUFFER);
    }

    protected Single<Finance> financeFullFromRowSet(RowIterator<Row> iterator) throws Exception {

        if(iterator.hasNext()) {
            var row = iterator.next();

            return transformerFromRowSet(row, iterator)
                .collectInto(new Vector<Transformer>(), Vector::add)
                .map(transformers -> new Finance(
                    row.getLong("finance_id"),
                    row.getDouble("balance"),
                    row.getDouble("interest"),
                    transformers
                ));

        } else { throw new Exception("No row found"); }

    }

    protected Single<Finance> financeFullFromRowSet(Row init, RowIterator<Row> iterator) {

        return transformerFromRowSet(init, iterator)
            .collectInto(new Vector<Transformer>(), Vector::add)
            .map(transformers -> new Finance(
                init.getLong("finance_id"),
                init.getDouble("balance"),
                init.getDouble("interest"),
                transformers
            ));
    }
}
