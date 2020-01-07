CREATE TABLE WAUSER (
  ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  USERNAME VARCHAR(64) NOT NULL,
  EMAIL VARCHAR(64),
  PASSWORD VARCHAR(64) NOT NULL,
  ROLES VARCHAR(64),
  ACTIVE BIGINT NOT NULL
);

CREATE TABLE WAGROUP (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    GROUPNAME VARCHAR(64) NOT NULL,
    OWNERID BIGINT NOT NULL,
    CATEGORY VARCHAR(64),
    DESCRIPTION VARCHAR(144)
);

CREATE TABLE WORKOUT (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(64) NOT NULL,
    WDATE DATE,
    WTIME BIGINT,
    PLACE VARCHAR(64),
    DESCRIPTION VARCHAR(64),
    CATEGORY VARCHAR(64)
);

CREATE TABLE EXERCISE (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(64) NOT NULL,
    MINUTES BIGINT,
    METER BIGINT,
    CALORIES BIGINT,
    WEIGHT BIGINT,
    REPS BIGINT,
    SETS BIGINT,
    CADENCE BIGINT
);


CREATE TABLE GOALS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    GOALNAME VARCHAR(64) NOT NULL,
    DEADLINE DATE,
    GROUPID BIGINT,
    COMPLETED BIT
);


CREATE TABLE USERGROUPCONNECTION (
    USERID BIGINT,
    GROUPID BIGINT,
    PRIMARY KEY (USERID, GROUPID),
    USERROLE VARCHAR(64) NOT NULL
);


CREATE TABLE WORKOUTEXERCISECONNECTION (
  WORKOUTID  BIGINT,
  EXERCISEID BIGINT,
  PRIMARY KEY (WORKOUTID, EXERCISEID)
);



CREATE TABLE USERWORKOUTCONNECTION (
   USERID BIGINT,
   WORKOUTID BIGINT,
   PRIMARY KEY(USERID, WORKOUTID)
);




ALTER TABLE GOALS ADD FOREIGN KEY (GROUPID) REFERENCES WAGROUP(ID);
ALTER TABLE WAGROUP ADD FOREIGN KEY (OWNERID) REFERENCES WAUSER(ID);
ALTER TABLE USERGROUPCONNECTION ADD FOREIGN KEY (GROUPID) REFERENCES WAGROUP(ID);
ALTER TABLE USERGROUPCONNECTION ADD FOREIGN KEY (USERID) REFERENCES WAUSER(ID);
ALTER TABLE WORKOUTEXERCISECONNECTION ADD FOREIGN KEY (WORKOUTID) REFERENCES WORKOUT(ID);
ALTER TABLE WORKOUTEXERCISECONNECTION ADD FOREIGN KEY (EXERCISEID) REFERENCES EXERCISE(ID);
ALTER TABLE USERWORKOUTCONNECTION ADD FOREIGN KEY (USERID) REFERENCES WAUSER(ID);
ALTER TABLE USERWORKOUTCONNECTION ADD FOREIGN KEY (WORKOUTID) REFERENCES WORKOUT(ID);







