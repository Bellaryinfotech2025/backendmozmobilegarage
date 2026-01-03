package com.mozworld.controller;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.mozworld.entity.IssueEntity;
import com.mozworld.service.IssueService;

import java.util.Map;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "http://localhost:5173")  
public class IssueController {

    @Autowired
    private IssueService issueService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createIssue(@RequestBody IssueEntity issue) {
        IssueEntity saved = issueService.createIssue(issue);
        return ResponseEntity.ok(Map.of("trackingId", saved.getTrackingId()));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<IssueEntity> getIssue(@PathVariable String trackingId) {
        try {
            IssueEntity issue = issueService.getIssueByTrackingId(trackingId);
            return ResponseEntity.ok(issue);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}