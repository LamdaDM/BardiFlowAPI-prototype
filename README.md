# About
An initial prototype for BardiFlowAPI, a web service for projecting cash flow. 
Current iteration uses MySQL for persistence, and Redis for caching.

Next iteration of BardiFlowAPI is planned have the following changes:
- Write in C#, using ASP.NET Core.
- Log to centralised logger (Trunk).
- Password hashing provided by Trunk, which uses Argon2i.
- Create separate standalone library for projection.
- Rework data structures.
- Report projections every interval over *x* intervals of *y* length, with an optional delay of *z* days (end of event line is instead *x \* y + z*).
- Generate chronological history of events for reloading projection from any event at day *n*.
