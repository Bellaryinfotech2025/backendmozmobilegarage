package com.mozworld.service;

import com.mozworld.entity.IssueEntity;

public interface IssueService {
    IssueEntity createIssue(IssueEntity issue);
    IssueEntity getIssueByTrackingId(String trackingId);
}