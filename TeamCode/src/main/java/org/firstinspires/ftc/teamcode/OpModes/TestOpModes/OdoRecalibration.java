package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Autonomous
public class OdoRecalibration extends LinearOpMode {
    public void runOpMode(){
        Hardware.init(hardwareMap);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            Hardware.pp.recalibrateIMU();
            requestOpModeStop();
        }
    }
}
