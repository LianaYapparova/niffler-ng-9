package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String authJdbcUrl();

  String userdataJdbcUrl();

  String spendUrl();

  String spendJdbcUrl();

  String currencyJdbcUrl();

  String ghUrl();

  String authUrl();
  String gatewayUrl();
  String userdataUrl();
}
