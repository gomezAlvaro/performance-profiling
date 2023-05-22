package victor.training.performance.profiling;

import eu.rekawek.toxiproxy.Proxy;
import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.ToxicDirection;

import java.io.IOException;

public class ToxiProxyUtil {
  public static void main(String[] args) throws IOException {
    configureToxiProxy();
  }

  public static void configureToxiProxy() throws IOException {
    // for this to work, please install ToxiProxy locally: https://github.com/shopify/toxiproxy#usage
    ToxiproxyClient client = new ToxiproxyClient("localhost", 8474);

    Proxy oldProxy = client.getProxyOrNull("toxi-proxy");
    if (oldProxy != null) {
      oldProxy.delete();
      System.out.println("Deleted old Toxiproxy");
    }

    Proxy proxy = client.createProxy("toxi-proxy", "localhost:55432", "localhost:5432");
    System.out.println("Created Toxiproxy");

    proxy.toxics().latency("real-latency", ToxicDirection.DOWNSTREAM, 3);
    System.out.println("Configured Toxiproxy");
  }

}