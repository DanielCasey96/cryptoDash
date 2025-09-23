# Crypto Aggregator \- Spring Boot \+ Gradle

## Overview
A Spring Boot service that aggregates free crypto market data and converts prices to a user\-selected fiat currency using a free FX rates API. Users can follow specific assets and view a personalized watchlist.

## Architecture
1. Inbound: REST controllers for public markets and user pages.
2. Core: Services for market aggregation, FX conversion, and watchlist management.
3. Outbound: HTTP clients to crypto market and FX APIs.
4. Storage: Relational DB for users, assets, watchlists; optional cache for prices.
5. Background: Schedulers to refresh top\-N markets and FX rates.
6. Security: Token\-based auth for user endpoints.

## External APIs \(\- free tiers\)
- Crypto markets: CoinGecko, CoinCap, CryptoCompare \(choose primary, add fallback\).
- FX rates: Frankfurter.app, exchangerate.host, or ECB daily rates.
- Normalize prices to a base currency \(e.g., USD\) then convert via FX rate.

## Data Flow
1. Fetch top market data \(price, market cap, 24h change\) in base currency.
2. Fetch FX rate base\-\>target.
3. Convert: `price_target = price_base * fx(base->target)`.
4. Cache both markets and FX rates to minimize API calls.

## Core Features
- Public top\-10 markets with selectable fiat.
- User watchlist \(follow/unfollow\) with converted prices.
- Optional real\-time updates via SSE/WebSocket for price deltas.

## REST Endpoints
- GET `/api/markets/top?limit=10&fiat=GBP`
- GET `/api/markets/{assetId}?fiat=GBP`
- GET `/api/user/watchlist?fiat=GBP`
- POST `/api/user/watchlist/{assetId}`
- DELETE `/api/user/watchlist/{assetId}`

## Data Model \(\- minimal\)
- User: id, email/handle, auth fields.
- CryptoAsset: id \(provider ID\), symbol, name.
- WatchlistItem: userId, assetId, createdAt \(unique userId\+assetId\).
- PriceSnapshot \(optional cache\): assetId, baseCurrency, price, marketCap, change24h, fetchedAt.
- FxRate: baseCurrency, quoteCurrency, rate, fetchedAt.

## Caching \& Rate Limits
- In\-memory cache for top\-N and asset lookups \(TTL ~30\-60s\).
- Cache FX rates longer \(~1h\); scheduled refresh.
- Per\-IP and per\-user rate limiting.
- Retries with jitter, exponential backoff, and fallback provider.

## Resilience \& Normalization
- Central resolver to map symbols to provider IDs.
- Validate fiat codes; default to base currency on unsupported.
- Currency\-aware rounding/formatting; handle very small prices \(high precision\).

## Security
- JWT\-style token auth for user endpoints.
- Principle of least privilege; no private keys stored.
- Enforce access control on watchlist operations.

## Background Jobs
- Refresh top\-N markets on schedule.
- Refresh FX rates on schedule.
- Optional warm\-up on startup.

## UI Behavior
- Main page: top\-10 list, currency selector, last updated time, 24h change.
- User page: followed assets with same fields; follow/unfollow actions.

## Setup \& Run \(Windows\)
1. Requirements: JDK 21\+, Git, IntelliJ IDEA 2025\.2\.2.
2. Configure environment \(examples\):
    - `APP_BASE_CURRENCY=USD`
    - `CRYPTO_API_BASE_URL=...`
    - `FX_API_BASE_URL=...`
    - `JWT_SECRET=...`
3. Build: `.\gradlew build`
4. Test: `.\gradlew test`
5. Run: `.\gradlew bootRun`
6. Default service port: 8080.

## Testing
- Unit tests for services with mocked clients.
- Contract/integration tests for API clients via mock HTTP server.
- Controller integration tests with mocked services.
- Load tests for `/api/markets/top` to validate cache effectiveness.

## Observability \& Ops
- Metrics for cache hit/miss, API latency, error rates.
- Structured logs for outbound calls with request IDs.
- Feature flags to switch providers dynamically.

## Edge Cases
- Missing/delisted asset IDs.
- Serve cached data with a `stale=true` flag if providers are down.
- FX rate unavailable: fall back to base currency and annotate response.
- Timezone alignment for 24h windows with provider data.
