package output.time_series;

import com.msiops.ground.either.Either;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DailyAdjusted implements Response {

  private final Map<String, String> metaData;
  private final List<StockData> stocks;

  private DailyAdjusted(Map<String, String> metaData, List<StockData> stocks) {
    this.metaData = metaData;
    this.stocks = stocks;
  }

  @Override
  public Map<String, String> getMetaData() {
    return metaData;
  }

  @Override
  public List<StockData> getStockData() {
    return stocks;
  }

  public static Either<DailyAdjusted, Exception> from(String json) {
    Parser parser = new Parser();
    return parser.parseJson(json);
  }

  private static class Parser extends TimeSeriesParser<DailyAdjusted> {

    @Override
    String getStockDataKey() {
      return "Time Series (Daily)";
    }

    @Override
    DailyAdjusted resolve(Map<String, String> metaData, Map<String, Map<String, String>> stockData) {
      List<StockData> stocks = new ArrayList<>();
      stockData.forEach((key, values) -> stocks.add(new StockData(
              DateTime.parse(key, DATE_FORMAT),
              Double.parseDouble(values.get("1. open")),
              Double.parseDouble(values.get("2. high")),
              Double.parseDouble(values.get("3. low")),
              Double.parseDouble(values.get("4. close")),
              Double.parseDouble(values.get("5. adjusted close")),
              Long.parseLong(values.get("6. volume")),
              Double.parseDouble(values.get("7. dividend amount")),
              Double.parseDouble(values.get("8. split coefficient"))

      )));
      return new DailyAdjusted(metaData, stocks);
    }

  }
}