package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Intake.Pitch;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
@TeleOp
public class PitchTest extends LinearOpMode {
    Pitch pitch;
    public enum State{
        TRANSFER,
        IDLE,
    };
    public static State state = State.IDLE;
    @Override
    public void runOpMode(){
        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        pitch = new Pitch();
        waitForStart();
        while (opModeIsActive()){
            if (state == State.TRANSFER && pitch.getState()!= Pitch.State.GOING_SHOOT && pitch.getState()!= Pitch.State.SHOOT) pitch.setState(Pitch.State.GOING_SHOOT);
            if (state == State.TRANSFER && pitch.getState()!= Pitch.State.GOING_IDLE && pitch.getState()!= Pitch.State.IDLE) pitch.setState(Pitch.State.GOING_IDLE);
            pitch.update();
            telemetry.addData("State",pitch.getState());
            telemetry.update();

        }
    }
}
