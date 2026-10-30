package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Intake.Ramp;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
@Config
@TeleOp
public class RampTest extends LinearOpMode {
    public enum State{
        DOWN,
        UP,
    };
    public static State state = State.DOWN;
    Ramp ramp;
    @Override
    public void runOpMode(){
        Hardware.init(hardwareMap);
        ramp = new Ramp();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        waitForStart();
        while (opModeIsActive()){
            if (state == State.UP && ramp.getState() != Ramp.State.UP && ramp.getState() != Ramp.State.GOING_UP) ramp.setState(Ramp.State.GOING_UP);
            if (state == State.DOWN && ramp.getState() != Ramp.State.DOWN && ramp.getState() != Ramp.State.GOING_DOWN) ramp.setState(Ramp.State.GOING_DOWN);
            ramp.update();
            telemetry.addData("Ramp State",ramp.getState());
            telemetry.addData("Current State",state);
            telemetry.update();
        }
    }
}
