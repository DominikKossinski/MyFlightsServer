-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema myflights
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `myflights` ;

-- -----------------------------------------------------
-- Schema myflights
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `myflights` DEFAULT CHARACTER SET utf8 ;
USE `myflights` ;

-- -----------------------------------------------------
-- Table `myflights`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `myflights`.`Users` ;

CREATE TABLE IF NOT EXISTS `myflights`.`Users` (
  `UserId` VARCHAR(28) NOT NULL,
  `Nick` VARCHAR(45) NULL,
  `Email` VARCHAR(45) NOT NULL,
  `ImageUrl` VARCHAR(200) NULL,
  PRIMARY KEY (`UserId`),
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `myflights`.`Airplanes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `myflights`.`Airplanes` ;

CREATE TABLE IF NOT EXISTS `myflights`.`Airplanes` (
  `AirplaneId` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `MaxSpeed` INT NULL,
  `Weight` INT NULL,
  `ImageUrl` VARCHAR(200) NULL,
  PRIMARY KEY (`AirplaneId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `myflights`.`Airports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `myflights`.`Airports` ;

CREATE TABLE IF NOT EXISTS `myflights`.`Airports` (
  `AirportId` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `City` VARCHAR(100) NOT NULL,
  `Shortcut` VARCHAR(4) NOT NULL,
  `TowerFrequency` VARCHAR(45) NULL,
  `GroundFrequency` VARCHAR(45) NULL,
  `ImageUrl` VARCHAR(45) NULL,
  PRIMARY KEY (`AirportId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `myflights`.`Runway`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `myflights`.`Runways` ;

CREATE TABLE IF NOT EXISTS `myflights`.`Runways` (
  `RunwayId` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Length` VARCHAR(45) NOT NULL,
  `Heading` INT NOT NULL,
  `ILSFrequency` VARCHAR(45) NULL,
  `ImageUrl` VARCHAR(45) NULL,
  `AirportId` INT NOT NULL,
  PRIMARY KEY (`RunwayId`, `AirportId`),
  INDEX `fk_Runway_Airport1_idx` (`AirportId` ASC),
  CONSTRAINT `fk_Runway_Airport1`
    FOREIGN KEY (`AirportId`)
    REFERENCES `myflights`.`Airports` (`AirportId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `myflights`.`Flights`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `myflights`.`Flights` ;

CREATE TABLE IF NOT EXISTS `myflights`.`Flights` (
  `FlightId` INT NOT NULL AUTO_INCREMENT,
  `Note` VARCHAR(500) NULL,
  `Distance` INT NULL,
  `ImageUrl` VARCHAR(200) NULL,
  `EndDate` DATETIME NOT NULL,
  `StartDate` DATETIME NOT NULL,
  `UserId` VARCHAR(28) NOT NULL,
  `AirplaneId` INT NOT NULL,
  `DepartureRunwayId` INT NOT NULL,
  `DepartureAirportId` INT NOT NULL,
  `ArrivalRunwayId` INT NOT NULL,
  `ArrivalAirportId` INT NOT NULL,
  PRIMARY KEY (`FlightId`, `UserId`, `AirplaneId`, `DepartureRunwayId`, `DepartureAirportId`, `ArrivalRunwayId`, `ArrivalAirportId`),
  INDEX `fk_Flight_Users_idx` (`UserId` ASC),
  INDEX `fk_Flight_Airplanes1_idx` (`AirplaneId` ASC),
  INDEX `fk_Flight_Runway1_idx` (`DepartureRunwayId` ASC, `DepartureAirportId` ASC),
  INDEX `fk_Flight_Runway2_idx` (`ArrivalRunwayId` ASC, `ArrivalAirportId` ASC),
  CONSTRAINT `fk_Flight_Users`
    FOREIGN KEY (`UserId`)
    REFERENCES `myflights`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Flight_Airplanes1`
    FOREIGN KEY (`AirplaneId`)
    REFERENCES `myflights`.`Airplanes` (`AirplaneId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Flight_Runway1`
    FOREIGN KEY (`DepartureRunwayId` , `DepartureAirportId`)
    REFERENCES `myflights`.`Runways` (`RunwayId` , `AirportId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Flight_Runway2`
    FOREIGN KEY (`ArrivalRunwayId` , `ArrivalAirportId`)
    REFERENCES `myflights`.`Runways` (`RunwayId` , `AirportId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
