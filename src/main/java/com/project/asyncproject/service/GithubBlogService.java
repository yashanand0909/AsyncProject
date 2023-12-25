package com.project.asyncproject.service;

import com.project.asyncproject.models.User;
import java.util.concurrent.CompletableFuture;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubBlogService {

  private final RestTemplate restTemplate;

  public GithubBlogService(
      RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Async("threadPoolExecutor")
  public CompletableFuture<User> getGitHubBlog(String id) throws InterruptedException {
    String url = String.format("https://api.github.com/users/%s", id);
    User user = restTemplate.getForObject(url,User.class);
    Thread.sleep(1000L);
    return CompletableFuture.completedFuture(user);
  }
}
