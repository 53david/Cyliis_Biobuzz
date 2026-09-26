package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Autonomous
public class RedSolo extends LinearOpMode {
    org.firstinspires.ftc.teamcode.LogicNodes.RedSolo redSolo;
    public void runOpMode(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        redSolo = new org.firstinspires.ftc.teamcode.LogicNodes.RedSolo(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            redSolo.update();
            telemetry.addData("Current Node",redSolo.currentNode.getName());
            telemetry.addData("X", Odo.predictedX);
            telemetry.addData("Y", Odo.predictedY);
            telemetry.addData("Heading", Odo.heading);
            telemetry.update();
        }
    }
}
