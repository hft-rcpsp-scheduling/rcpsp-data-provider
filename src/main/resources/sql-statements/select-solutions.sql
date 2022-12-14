SELECT s.id            AS id,
       s.creation_date AS date,
       s.creation_time AS time,
       s.creator       AS creator,
       s.makespan      AS makespan,
       p.size          AS size,
       p.par           AS par,
       p.inst          AS inst,
       p.r1_capacity   AS r1,
       p.r2_capacity   AS r2,
       p.r3_capacity   AS r3,
       p.r4_capacity   AS r4,
       p.horizon       AS horizon,
       p.job_count     AS jobCount,
       j.nr            AS jNr,
       j.suc_count     AS jSucCount,
       j.successors    AS jSuc,
       j.pre_count     AS jPreCount,
       j.predecessors  AS jPre,
       j.mode          AS jMode,
       j.duration      AS jDuration,
       j.r1            AS jR1,
       j.r2            AS jR2,
       j.r3            AS jR3,
       j.r4            AS jR4,
       sd.start        AS jStart
FROM solutions s
         JOIN projects p on p.id = s.project_id
         JOIN solution_details sd on s.id = sd.solution_id
         JOIN jobs j on p.id = j.project_id and sd.job_id = j.id
