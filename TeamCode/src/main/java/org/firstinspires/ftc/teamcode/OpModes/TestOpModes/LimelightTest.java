package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Limelight;
@TeleOp
public class LimelightTest extends LinearOpMode {
    Limelight limelight;
    public static int index;
    public void runOpMode() throws InterruptedException{
        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        limelight = new Limelight(Limelight.State.STOP);
        limelight.setPipelineIndex(index);
        while (opModeInInit()){
            limelight.setState(Limelight.State.ACTIVE);
        }
        while (opModeIsActive()){
            limelight.update();
            if (index == 0) {
                telemetry.addData("X", limelight.getPosX());
                telemetry.addData("Y", limelight.getPosY());
                telemetry.addData("Z", limelight.getPosZ());
            }
            telemetry.update();
        }
    }
}
