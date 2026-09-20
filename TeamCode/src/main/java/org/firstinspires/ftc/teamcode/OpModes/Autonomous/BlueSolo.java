package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Autonomous
public class BlueSolo extends LinearOpMode {
    org.firstinspires.ftc.teamcode.LogicNodes.BlueSolo blueSolo;
    @Override
    public void runOpMode(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        blueSolo = new org.firstinspires.ftc.teamcode.LogicNodes.BlueSolo(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            blueSolo.update();
            telemetry.addData("Current Node",blueSolo.currentNode.getName());
            telemetry.addData("X", Odo.getX());
            telemetry.addData("Y", Odo.getY());
            telemetry.addData("Heading", Odo.getHeading());
            telemetry.update();
        }
    }
}
