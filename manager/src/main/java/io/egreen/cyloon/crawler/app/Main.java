package io.egreen.cyloon.crawler.app;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.AttachContainerResultCallback;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by dewmal on 10/12/15.
 */
public class Main extends ResourceConfig {
    public Main() {
        register(JacksonFeature.class);
    }


    private static long errors = 0;
    private static long sucess = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withUri("http://apps.egreenhive.com:4243")
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();


        File baseDir = new File("fromapp");

        System.out.println(baseDir.getAbsolutePath());

        BuildImageResultCallback callback = new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                System.out.println("" + item);
                super.onNext(item);
            }
        };

        BuildImageResultCallback imageResultCallback = dockerClient.buildImageCmd(baseDir).exec(callback);
        String imageId = imageResultCallback.awaitImageId();
        System.out.println(imageId);

        CreateContainerResponse container = dockerClient.createContainerCmd(imageId)
                .withCmd("true")
                .withTty(true)
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

//        LOG.info("Created container: {}", container.toString());
//        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();

//        assertThat(exitCode, equalTo(0));


        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        while (inspectContainerResponse.getState().isRunning()) {
            System.out.println("Running");
        }

        System.out.println(inspectContainerResponse.getState().getStartedAt());
        System.out.println(inspectContainerResponse.getState().getFinishedAt());

//


        LogContainerResultCallback loggingCallback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                System.out.println(item);
                super.onNext(item);
            }
        };
        // this essentially test the since=0 case
        dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec(loggingCallback);

//        loggingCallback.awaitCompletion();


        ExecStartCmd exec = dockerClient.execStartCmd(container.getId());
        exec.withTty(true);


    }

    public static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "UTF-8")) {
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        } catch (UnsupportedEncodingException ex) {
        /* ... */
        } catch (IOException ex) {
        /* ... */
        }
        return out.toString();
    }
}
