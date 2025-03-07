package io.micronaut.views.turbo

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.io.Writable
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.views.View
import io.micronaut.views.ViewsRenderer
import io.micronaut.views.turbo.http.TurboHttpHeaders
import jakarta.inject.Singleton
import spock.lang.Specification

class TurboFrameSpec extends Specification {

    void "TurboFrame src"() {
        expect:
        '<turbo-frame src="/foo"></turbo-frame>' == toString(TurboFrame.builder().src("/foo").build())
    }

    void "TurboFrame target"() {
        expect:
        '<turbo-frame target="_top"></turbo-frame>' == toString(TurboFrame.builder().target("_top").build())
    }

    void "TurboFrame id"() {
        expect:
        '<turbo-frame id="main"></turbo-frame>' == toString(TurboFrame.builder().id("main").build())
    }

    void "TurboFrame loading"() {
        expect:
        '<turbo-frame loading="eager"></turbo-frame>' == toString(TurboFrame.builder().loading(Loading.EAGER).build())
        '<turbo-frame loading="lazy"></turbo-frame>' == toString(TurboFrame.builder().loading(Loading.LAZY).build())
    }

    void "TurboFrame busy"() {
        expect:
        '<turbo-frame busy="true"></turbo-frame>' == toString(TurboFrame.builder().busy(true).build())
        '<turbo-frame busy="false"></turbo-frame>' == toString(TurboFrame.builder().busy(false).build())
    }

    void "TurboFrame autoscroll"() {
        expect:
        '<turbo-frame autoscroll="true"></turbo-frame>' == toString(TurboFrame.builder().autoscroll(true).build())
        '<turbo-frame autoscroll="false"></turbo-frame>' == toString(TurboFrame.builder().autoscroll(false).build())
    }

    void "TurboFrame disabled"() {
        expect:
        '<turbo-frame disabled="true"></turbo-frame>' == toString(TurboFrame.builder().disabled(true).build())
        '<turbo-frame disabled="false"></turbo-frame>' == toString(TurboFrame.builder().disabled(false).build())
    }

    void "TurboFrame visit action"() {
        expect:
        '<turbo-frame data-turbo-action="advance"></turbo-frame>' == toString(TurboFrame.builder().visitAction(VisitAction.ADVANCE).build())
        '<turbo-frame data-turbo-action="restore"></turbo-frame>' == toString(TurboFrame.builder().visitAction(VisitAction.RESTORE).build())
        '<turbo-frame data-turbo-action="replace"></turbo-frame>' == toString(TurboFrame.builder().visitAction(VisitAction.REPLACE).build())
    }

    void "you can combine TurboFrameView and View"() {
        given:
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, ['spec.name': 'TurboFrameSpec'])
        HttpClient httpClient = embeddedServer.applicationContext.createBean(HttpClient, embeddedServer.URL)
        BlockingHttpClient client = httpClient.toBlocking()
        when:
        String html = client.retrieve(HttpRequest.GET('/frame'), String)

        then:
        html == "<!DOCTYPE html><html><head><title>Page Title</title></head><body><turbo-frame id=\"main\"><div class=\"message\">Hello world</div></turbo-frame></body></html>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/eager').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" loading=\"eager\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/eager/withbuilder').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" loading=\"eager\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/eager/withoutbuilder').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" loading=\"eager\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/lazy').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" loading=\"lazy\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/src').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" src=\"/foo\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/target').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" target=\"_target\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/busy').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" busy=\"true\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/busyFalse').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" busy=\"false\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/disabled').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" disabled=\"true\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/autoscroll').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" autoscroll=\"true\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/id').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"foo\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/restore').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" data-turbo-action=\"restore\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/advance').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" data-turbo-action=\"advance\"><div class=\"message\">Hello world</div></turbo-frame>"

        when:
        html = client.retrieve(HttpRequest.GET('/frame/replace').header(TurboHttpHeaders.TURBO_FRAME, "main"), String)

        then:
        html == "<turbo-frame id=\"main\" data-turbo-action=\"replace\"><div class=\"message\">Hello world</div></turbo-frame>"

        cleanup:
        httpClient.close()
        embeddedServer.close()
    }

    @NonNull
    private static String toString(@NonNull TurboFrame turboFrame) {
        Writable writable = turboFrame.render().get()
        StringWriter stringWriter = new StringWriter()
        writable.writeTo(stringWriter)
        return stringWriter.toString()
    }

    @Requires(property = "spec.name", value = "TurboFrameSpec")
    @Controller("/frame")
    static class TurboFrameController {
        @Get
        @TurboFrameView("fragments/_messages")
        @View("home")
        String index() {
            "Hello world"
        }

        @Get("/eager")
        @TurboFrameView(value = "fragments/_messages", loading = "eager")
        String eager() {
            "Hello world"
        }

        @Get("/eager/withbuilder")
        TurboFrame.Builder eagerWithBuilder(@Header(TurboHttpHeaders.TURBO_FRAME) String frame) {
            (TurboFrame.Builder) TurboFrame.builder()
                    .loading(Loading.EAGER)
                    .id(frame)
                    .template("fragments/_messages", "Hello world");
        }

        @Get("/eager/withoutbuilder")
        TurboFrame eagerWithoutBuilder(@Header(TurboHttpHeaders.TURBO_FRAME) String frame) {
            TurboFrame.builder()
                    .loading(Loading.EAGER)
                    .id(frame)
                    .template("<div class=\"message\">Hello world</div>")
            .build()
        }

        @Get("/advance")
        @TurboFrameView(value = "fragments/_messages", action = "advance")
        String advance() {
            "Hello world"
        }

        @Get("/restore")
        @TurboFrameView(value = "fragments/_messages", action = "restore")
        String restore() {
            "Hello world"
        }

        @Get("/replace")
        @TurboFrameView(value = "fragments/_messages", action = "replace")
        String replace() {
            "Hello world"
        }

        @Get("/lazy")
        @TurboFrameView(value = "fragments/_messages", loading = "lazy")
        String lazy() {
            "Hello world"
        }

        @Get("/target")
        @TurboFrameView(value = "fragments/_messages", target = "_target")
        String target() {
            "Hello world"
        }

        @Get("/src")
        @TurboFrameView(value = "fragments/_messages", src = "/foo")
        String src() {
            "Hello world"
        }

        @Get("/busy")
        @TurboFrameView(value = "fragments/_messages", busy = StringUtils.TRUE)
        String busy() {
            "Hello world"
        }

        @Get("/busyFalse")
        @TurboFrameView(value = "fragments/_messages", busy = StringUtils.FALSE)
        String busyFalse() {
            "Hello world"
        }

        @Get("/disabled")
        @TurboFrameView(value = "fragments/_messages", disabled = StringUtils.TRUE)
        String disabled() {
            "Hello world"
        }

        @Get("/autoscroll")
        @TurboFrameView(value = "fragments/_messages", autoscroll = StringUtils.TRUE)
        String autoscroll() {
            "Hello world"
        }

        @Get("/id")
        @TurboFrameView(value = "fragments/_messages", id="foo")
        String id() {
            "Hello world"
        }
    }

    @Requires(property = "spec.name", value = "TurboFrameSpec")
    @Singleton
    static class MockViewsRenderer<T> implements ViewsRenderer<T> {

        @Override
        @NonNull
        Writable render(@NonNull String viewName,
                        @Nullable T data,
                        @Nullable HttpRequest<?> request) {
            if (viewName == 'home') {
                return new Writable() {
                    @Override
                    void writeTo(Writer out) throws IOException {
                        out.write("<!DOCTYPE html><html><head><title>Page Title</title></head><body><turbo-frame id=\"main\"><div class=\"message\">" + data.toString() + "</div></turbo-frame></body></html>")
                    }
                }
            } else if (viewName == 'fragments/_messages') {
                return (writer) -> {
                    if (data != null) {
                        writer.write("<div class=\"message\">")
                        writer.write(data.toString())
                        writer.write("</div>")
                    }
                }
            }
            return (writer) -> {
                if (data != null) {
                    writer.write(data.toString())
                }
            }
        }

        @Override
        boolean exists(@NonNull String viewName) {
            true
        }
    }
}
