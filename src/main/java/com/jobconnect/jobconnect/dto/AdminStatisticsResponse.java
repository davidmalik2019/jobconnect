package com.jobconnect.jobconnect.dto;

public class AdminStatisticsResponse {

    private long totalUsers;
    private long totalJobSeekers;
    private long totalEmployers;
    private long totalJobs;
    private long totalApplications;

    public AdminStatisticsResponse() {
    }

    public AdminStatisticsResponse(
            long totalUsers,
            long totalJobSeekers,
            long totalEmployers,
            long totalJobs,
            long totalApplications) {

        this.totalUsers = totalUsers;
        this.totalJobSeekers = totalJobSeekers;
        this.totalEmployers = totalEmployers;
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalJobSeekers() {
        return totalJobSeekers;
    }

    public long getTotalEmployers() {
        return totalEmployers;
    }

    public long getTotalJobs() {
        return totalJobs;
    }

    public long getTotalApplications() {
        return totalApplications;
    }
}