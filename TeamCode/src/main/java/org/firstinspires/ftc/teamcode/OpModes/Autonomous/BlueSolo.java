package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.LogicNodes.BlueAuto;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Autonomous
public class BlueSolo extends LinearOpMode {
    BlueAuto blueAuto;
    @Override
    public void runOpMode(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        blueAuto = new BlueAuto(hardwareMap);
        waitForStart();
        while (opModeIsActive()){

            blueAuto.update();

            telemetry.addData("Current Node", blueAuto.currentNode.getName());
            telemetry.addData("X", Odo.getX());
            telemetry.addData("Y", Odo.getY());
            telemetry.addData("Heading", Odo.getHeading());
            telemetry.update();
        }
    }
}
