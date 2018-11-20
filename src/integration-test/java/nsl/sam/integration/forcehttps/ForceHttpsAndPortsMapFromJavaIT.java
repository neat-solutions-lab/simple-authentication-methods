package nsl.sam.integration.forcehttps;

import nsl.sam.IntegrationTestConstants;
import nsl.sam.core.annotation.AuthenticationMethod;
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods;
import nsl.sam.integration.controller.IntegrationTestController;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.assertj.core.api.Assertions;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.SocketUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {ForceHttpsAndPortsMapFromJavaITConfiguration.class}
)
@EnableAutoConfiguration
@TestPropertySource(properties = {
        "server.ssl.key-store=src/integration-test/config/keystore.jks",
        "server.ssl.key-store-password=123456",
        "server.ssl.keyAlias=localhost"})
public class ForceHttpsAndPortsMapFromJavaIT {

    public static Integer httpPort = 0;
    public static Integer httpsPort = 0;

    @BeforeAll
    public static void beforeAll() {
        httpPort = SocketUtils.findAvailableTcpPort();
        httpsPort = SocketUtils.findAvailableTcpPort();
        System.setProperty("server.port", httpsPort.toString());
        System.setProperty("http.port", httpPort.toString());
        System.out.println("httpPort: " + httpPort);
        System.out.println("httpsPort: " + httpsPort);
    }


    @AfterAll
    public static void afterAll() {
        System.clearProperty("server.port");
        System.clearProperty("http.port");
    }

    @Test
    public void redirectResponseWhenInsecureChannelCalled() throws Exception  {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }
            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };
        sslContext.init(null, new TrustManager[] {trustManager}, null);

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        String urlString = "http://localhost:" + httpPort  + IntegrationTestConstants.INTEGRATION_TEST_ENDPOINT;

        Request request = new Request.Builder()
                .url(urlString)
                .build();

        Response response = client.newCall(request).execute();

        Assertions.assertThat(response.code()).isEqualTo(302);

        Assertions.assertThat(response.header("Location")).isEqualTo(
                "https://localhost:" + httpsPort + "/integration-test"
        );
    }

}

@Configuration
@EnableSimpleAuthenticationMethods(
        methods = {AuthenticationMethod.SIMPLE_BASIC_AUTH},
        forceHttps = true,
        portMapping = {PortsMappingJavaImpl.class}
)
@ComponentScan(basePackageClasses = {IntegrationTestController.class})
class ForceHttpsAndPortsMapFromJavaITConfiguration {

    @Bean
    public WebServerFactoryCustomizer<JettyServletWebServerFactory> customContainer() {
        return new CustomContainerForJavaTest();
    }

}

class CustomContainerForJavaTest implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    @Override
    public void customize(JettyServletWebServerFactory factory) {

        factory.addServerCustomizers(server -> {
            // HTTP
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(ForceHttpsAndPortsMapFromJavaIT.httpPort);

            // HTTPS
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setKeyStorePassword("123456");
            sslContextFactory.setKeyStorePath("src/integration-test/config/keystore.jks");
            sslContextFactory.setCertAlias("localhost");

            HttpConfiguration https = new HttpConfiguration();

            https.addCustomizer(new SecureRequestCustomizer());

            ServerConnector sslConnector = new ServerConnector(
                    server,
                    new SslConnectionFactory(sslContextFactory, "http/1.1"),
                    new HttpConnectionFactory(https)
            );
            sslConnector.setPort(ForceHttpsAndPortsMapFromJavaIT.httpsPort);
            server.setConnectors(new Connector[]{connector, sslConnector});
        });
    }
}
