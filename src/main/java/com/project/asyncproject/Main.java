package com.project.asyncproject;

import com.project.asyncproject.models.User;
import com.project.asyncproject.service.GithubBlogService;
import java.util.concurrent.CompletableFuture;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class Main implements CommandLineRunner {

  private final GithubBlogService githubBlogService;

  public Main(GithubBlogService githubBlogService) {
    this.githubBlogService = githubBlogService;
  }

  @Bean("threadPoolExecutor")
  public TaskExecutor executor(){
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(3);
    taskExecutor.setMaxPoolSize(5);
    taskExecutor.setQueueCapacity(100);
    taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    taskExecutor.setThreadNamePrefix("Async-");
    return taskExecutor;
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    long start = System.currentTimeMillis();
    // Kick of multiple, asynchronous lookups
    CompletableFuture<User> page1 = githubBlogService.getGitHubBlog("PivotalSoftware");
    CompletableFuture<User> page2 = githubBlogService.getGitHubBlog("CloudFoundry");
    CompletableFuture<User> page3 = githubBlogService.getGitHubBlog("Spring-Projects");
    CompletableFuture<User> page4 = githubBlogService.getGitHubBlog("AlanBinu007");

    CompletableFuture<User> page5 = CompletableFuture.runAsync(() -> {
      try {
        githubBlogService.getGitHubBlog("PivotalSoftware");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).newIncompleteFuture();


    System.out.println();

    System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));
    System.out.println("Info --> "+page1.get().toString());
    System.out.println("Info --> "+page2.get().toString());
    System.out.println("Info --> "+page3.get().toString());
    System.out.println("Info --> "+page4.get().toString());
  }
}
