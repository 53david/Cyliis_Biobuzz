package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Shooter.Flywheel;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@TeleOp
public class FlywheelTuner extends LinearOpMode {
    Flywheel flywheel;
    public void runOpMode(){
        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        flywheel = new Flywheel();
        flywheel.setState(Flywheel.State.IDLE);
        while (opModeIsActive()){
            flywheel.update();
            telemetry.addData("Current Velocity",flywheel.getVelocity());
            telemetry.addData("Target Velocity",flywheel.getTargetVelocity());
            telemetry.update();
        }
    }
}
