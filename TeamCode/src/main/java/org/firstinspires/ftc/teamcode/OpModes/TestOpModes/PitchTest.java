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
    public static int x = 0;
    @Override
    public void runOpMode(){
        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        pitch = new Pitch();
        waitForStart();
        while (opModeIsActive()){
            pitch.update();
            if (x==0)pitch.setState(Pitch.State.IDLE);
            if (x==1)pitch.setState(Pitch.State.SHOOT);
            telemetry.addData("State",pitch.getState());
            telemetry.update();

        }
    }
}
