package com.hft.provider.eval;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SolutionEvaluator {

    /**
     * @param solution to evaluate
     * @param feedback feedback with matching identifier and given makespan
     * @return feedback with full evaluation
     */
    public static Feedback evaluate(Project solution, Feedback feedback) {
        if (!checkStartDays(solution)) {
            feedback.setFeasible(false);
            feedback.setNotFeasibleReason("Start days of the jobs are missing.");
            return feedback;
        }
        feedback.setFeasible(true);
        String notFeasibleReason = "";
        if (!evaluateRelationships(solution)) {
            feedback.setFeasible(false);
            notFeasibleReason += "The relationships are an issue.";
        }
        if (!evaluateResourceUsage(solution)) {
            feedback.setFeasible(false);
            if (!notFeasibleReason.isEmpty())
                notFeasibleReason += " ";
            notFeasibleReason += "The resource boundaries are an issue.";
        }
        if (!feedback.isFeasible())
            feedback.setNotFeasibleReason(notFeasibleReason);
        return feedback;
    }

    // === PRIVATE =====================================================================================================

    private static boolean checkStartDays(Project solution) {
        for (Job job : solution.getJobs()) {
            if (job.getStartDay() == null || job.getStartDay() < 0 || job.getStartDay() > solution.getHorizon())
                return false;
        }
        return true;
    }

    private static boolean evaluateRelationships(Project solution) {
        Map<Integer, Job> map = solution.getJobs().stream().collect(Collectors.toMap(Job::getNr, Function.identity()));
        for (Job job : map.values()) {
            int endDay = job.getStartDay() + job.getDurationDays();
            for (int successor : job.getSuccessors()) {
                if (map.get(successor) == null || map.get(successor).getStartDay() < endDay)
                    return false;
            }
        }
        return true;
    }

    private static boolean evaluateResourceUsage(Project solution) {
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
            if (usagePerDay[0][day] > solution.getR1CapacityPerDay() ||
                    usagePerDay[1][day] > solution.getR2CapacityPerDay() ||
                    usagePerDay[2][day] > solution.getR3CapacityPerDay() ||
                    usagePerDay[3][day] > solution.getR4CapacityPerDay())
                return false;
        }
        return true;
    }
}
