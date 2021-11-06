package co.com.sofka.estebang.consumer;

import co.com.sofka.estebang.model.job.repository.JobExecutorRepository;
import com.google.gson.Gson;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
@RequiredArgsConstructor
public class RestConsumer implements JobExecutorRepository {

    @Override
    public Mono<String> sendHttpRequest(String url, int timeout, String method, String body) {
        if ("GET".equalsIgnoreCase(method))
            return testGet(url, timeout);
        else
            return testPost(url, timeout, body);
    }

    public Mono<String> testGet(String url, int timeout) {
        return getWebClient(url, timeout)
                .get()
                .exchangeToMono(this::getStatus);
    }

    public Mono<String> testPost(String url, int timeout, String body) {
        return getWebClient(url, timeout)
                .post()
                .bodyValue(getGson().toJson(body))
                .exchangeToMono(this::getStatus);
    }

    private Gson getGson() {
        return new Gson();
    }

    private Mono<String> getStatus(ClientResponse clientResponse) {
        return Mono.just(clientResponse.statusCode().name());
    }

    private WebClient getWebClient(String url, int timeout) {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(getClientHttpConnector(timeout))
                .build();
    }

    private ClientHttpConnector getClientHttpConnector(int timeout) {
        /*
        IF YO REQUIRE APPEND SSL CERTIFICATE SELF SIGNED
        SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();*/
        return new ReactorClientHttpConnector(HttpClient.create()
                //.secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
                .compress(true)
                .keepAlive(true));
                //.option(CONNECT_TIMEOUT_MILLIS, timeout);
//                .doOnConnected(connection -> {
//                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
//                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
//                }));
    }

}