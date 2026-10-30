package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Shooter.Hood;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@TeleOp
public class HoodTuner extends LinearOpMode {
    Hood hood;
    public void runOpMode(){
        Hardware.init(hardwareMap);
        hood = new Hood();
        hood.setState(Hood.State.PAUSE);
        waitForStart();
        while (opModeIsActive()){
            hood.update();
        }

    }
}
