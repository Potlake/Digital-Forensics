create database keystrokes;
USE keystrokes;

drop table if exists Users, features, originalFiles, duplicateFiles;
create table Users (
        id INT NOT NULL AUTO_INCREMENT,
        classifier VARCHAR(30) NOT NULL,
        PRIMARY KEY (ID)
);

create table features
(
		featureid INT NOT NULL AUTO_INCREMENT,
		inputkeys VARCHAR(300) NOT NULL,
        dwell_time long NOT NULL,
        down_flight_time long NOT NULL,
        up_flight_time long NOT NULL,
        dwell_time_mean long NOT NULL,
        down_flight_time_mean long NOT NULL,
        up_flight_time_mean long NOT NULL,
        dwell_time_stdeviation long NOT NULL,
        down_flight_time_stdeviation long NOT NULL,
        up_flight_time_stdeviation long NOT NULL,
        number_of_backspace int unsigned not null,
        userid INT NOT NULL REFERENCES Users(id),
        PRIMARY KEY(featureid)
);

create table originalFiles
(
	fileid INT NOT NULL AUTO_INCREMENT,
    filePath BLOB NOT NULL,
    hashvalue VARCHAR(100) NOT NULL,
    userid INT NOT NULL REFERENCES Users(id),
    PRIMARY KEY(fileid)
    
	
);

create table duplicateFiles
(
	duplicateid INT NOT NULL AUTO_INCREMENT,
    filePath BLOB NOT NULL,
    hashvalue VARCHAR(100) NOT NULL,
    fileid INT NOT NULL REFERENCES originalFiles(fileid),
    PRIMARY KEY(duplicateid)
    
	
);

