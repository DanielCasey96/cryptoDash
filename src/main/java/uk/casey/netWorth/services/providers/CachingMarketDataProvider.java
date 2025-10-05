package uk.casey.netWorth.services.providers;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import uk.casey.netWorth.models.CoinGeckoResponseModel;

@Component
@Primary
public class CachingMarketDataProvider implements IMarketDataProvider {

  private final CoinGeckoProvider delegate;
  private final Duration ttl = Duration.ofSeconds(60);

  private final Map<String, Entry<List<CoinGeckoResponseModel>>> marketCache =
      new ConcurrentHashMap<>();
  private final Map<String, Entry<CoinGeckoResponseModel>> itemCache = new ConcurrentHashMap<>();

  public CachingMarketDataProvider(CoinGeckoProvider delegate) {
    this.delegate = delegate;
  }

  @Override
  public List<CoinGeckoResponseModel> getMarketList(String vsCurrency) {
    String key = "list:" + vsCurrency;
    Entry<List<CoinGeckoResponseModel>> cached = marketCache.get(key);
    if (isFresh(cached)) return cached.value;

    try {
      List<CoinGeckoResponseModel> fresh = delegate.getMarketList(vsCurrency);
      marketCache.put(key, new Entry<>(fresh));
      return fresh;
    } catch (RestClientException e) {
      if (cached != null) return cached.value;
      throw e;
    }
  }

  @Override
  public CoinGeckoResponseModel getItem(String itemId, String vsCurrency) {
    String key = "item:" + itemId + ":" + vsCurrency;
    Entry<CoinGeckoResponseModel> cached = itemCache.get(key);
    if (isFresh(cached)) return cached.value;

    try {
      CoinGeckoResponseModel fresh = delegate.getItem(itemId, vsCurrency);
      itemCache.put(key, new Entry<>(fresh));
      return fresh;
    } catch (RestClientException e) {
      if (cached != null) return cached.value;
      throw e;
    }
  }

  private boolean isFresh(Entry<?> e) {
    return e != null && Instant.now().isBefore(e.created.plus(ttl));
  }

  private static class Entry<T> {
    final T value;
    final Instant created = Instant.now();

    Entry(T value) {
      this.value = value;
    }
  }
}
