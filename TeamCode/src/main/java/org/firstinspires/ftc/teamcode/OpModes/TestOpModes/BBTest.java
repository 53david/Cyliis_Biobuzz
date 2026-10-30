package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Intake.Storage;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@TeleOp
public class BBTest extends LinearOpMode {
    Storage storage;
    public void runOpMode(){
        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        storage = new Storage();
        waitForStart();
        while (opModeIsActive()){

            storage.update();
            telemetry.addData("slot1",storage.getStateSensor1());
            telemetry.addData("slot2",storage.getStateSensor2());
            telemetry.addData("slot3",storage.getStateSensor3());
            telemetry.addData("slot4",storage.getStateSensor4());
            telemetry.update();
        }
    }
}
