package com.hft.provider.eval;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SolutionEvaluator {

    /**
     * @param solution to evaluate
     * @param feedback feedback with matching identifier and given makespan (record)
     * @return feedback with full evaluation
     */
    public static Feedback evaluate(Project solution, Feedback feedback) {
        String checkStartDays = evaluateStartDays(solution);

        if (!checkStartDays.isEmpty()) {
            feedback.setFeasible(false);
            feedback.setNotFeasibleReason(checkStartDays);
            return feedback;
        }

        Integer solutionTimeSpan = extractSolutionTimespan(solution);
        feedback.setSolutionTimeSpan(solutionTimeSpan);
        feedback.setNewRecord(solutionTimeSpan != null && solutionTimeSpan <= feedback.getRecordTimeSpan());

        String checkRelationships = evaluateRelationships(solution);
        String checkResources = evaluateResourceUsage(solution);
        String checkTimeSpan = evaluateTimespan(solution, solutionTimeSpan);

        String rejectionReason = concatReasons(List.of(checkRelationships, checkResources, checkTimeSpan));
        if (rejectionReason.isEmpty()) {
            feedback.setFeasible(true);
        } else {
            feedback.setFeasible(false);
            feedback.setNotFeasibleReason(rejectionReason);
        }
        return feedback;
    }

    // === PRIVATE =====================================================================================================

    /**
     * @param solution input data
     * @return failure reason or empty string
     */
    private static String evaluateStartDays(Project solution) {
        StringBuilder reason = new StringBuilder();
        for (Job job : solution.getJobs()) {
            if (job.getStartDay() == null) {
                reason.append("No Start Day (job=").append(job.getNr()).append(") ");
                continue;
            }
            if (job.getStartDay() < 0) {
                reason.append("Start Day < 0 (job=").append(job.getNr()).append(") ");
            }
            if (job.getStartDay() > solution.getHorizon()) {
                reason.append("Start Day > Horizon (job=").append(job.getNr()).append(") ");
            }
        }
        return reason.toString();
    }

    /**
     * @param solution input data
     * @return failure reason or empty string
     */
    private static String evaluateRelationships(Project solution) {
        StringBuilder reason = new StringBuilder();
        Map<Integer, Job> map = solution.getJobs().stream().collect(Collectors.toMap(Job::getNr, Function.identity()));
        for (Job job : map.values()) {
            int endDay = job.getStartDay() + job.getDurationDays();
            for (int successor : job.getSuccessors()) {
                if (map.get(successor) == null || map.get(successor).getStartDay() < endDay)
                    reason.append("(job=").append(job.getNr()).append(" and successor=").append(successor).append(") ");
            }
        }
        return reason.isEmpty() ? "" : "Relationships violated: " + reason;
    }

    /**
     * @param solution input data
     * @return failure reason or empty string
     */
    private static String evaluateResourceUsage(Project solution) {
        StringBuilder reason = new StringBuilder();
        int[][] usagePerDay = new int[4][solution.getHorizon()];
        for (Job job : solution.getJobs()) {
            Integer start = job.getStartDay();
            for (int day = start; day < start + job.getDurationDays(); day++) {
                usagePerDay[0][day] += job.getR1HoursPerDay();
                usagePerDay[1][day] += job.getR2HoursPerDay();
                usagePerDay[2][day] += job.getR3HoursPerDay();
                usagePerDay[3][day] += job.getR4HoursPerDay();
            }
        }
        for (int day = 0; day < usagePerDay[0].length; day++) {
            if (usagePerDay[0][day] > solution.getR1CapacityPerDay()) {
                reason.append("(r1 at day=").append(day).append(") ");
            }
            if (usagePerDay[1][day] > solution.getR2CapacityPerDay()) {
                reason.append("(r2 at day=").append(day).append(") ");
            }
            if (usagePerDay[2][day] > solution.getR3CapacityPerDay()) {
                reason.append("(r3 at day=").append(day).append(") ");
            }
            if (usagePerDay[3][day] > solution.getR4CapacityPerDay()) {
                reason.append("(r4 at day=").append(day).append(") ");
            }
        }
        return reason.isEmpty() ? "" : "Resources violated: " + reason;
    }

    /**
     * @param solution input data
     * @param timespan number or null
     * @return failure reason or empty string
     */
    private static String evaluateTimespan(Project solution, Integer timespan) {
        if (timespan == null) {
            return "No timespan could be calculated from the last job. ";
        } else if (timespan > solution.getHorizon()) {
            return "Timespan of the solution is larger than the horizon. ";
        } else {
            return "";
        }
    }

    /**
     * @param solution input data
     * @return start day of the lst job as timespan or null if not found
     */
    private static Integer extractSolutionTimespan(Project solution) {
        try {
            return solution.getJobs()
                    .stream().filter(job -> job.getNr() == solution.getJobs().size())
                    .findFirst().orElseThrow()
                    .getStartDay();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param reasons list with strings that can be null and empty
     * @return appended reasons with new line separators
     */
    private static String concatReasons(List<String> reasons) {
        StringBuilder concatReason = new StringBuilder();
        for (int i = 0; i < reasons.size(); i++) {
            String reason = reasons.get(i);
            if (reason != null && !reason.isEmpty()) {
                concatReason.append(reason.trim()).append(";");
                if (i + 1 < reasons.size() && !reasons.get(i + 1).isEmpty()) {
                    concatReason.append("\n");
                }
            }
        }
        return concatReason.toString();
    }
}
