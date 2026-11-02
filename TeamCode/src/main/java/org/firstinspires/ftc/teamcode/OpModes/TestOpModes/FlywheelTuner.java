package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Shooter.Flywheel;
import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Math.ShooterCalculator;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Config
@TeleOp
public class FlywheelTuner extends LinearOpMode {
    Flywheel flywheel;
    Turret turret;
    Odo odo;
    ShooterCalculator shooterCalculator;
    public static double power = 0;
    public static double pos = 0.35;
    public void runOpMode(){
        Hardware.init(hardwareMap);
        shooterCalculator = new ShooterCalculator();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        flywheel = new Flywheel();
        flywheel.setState(Flywheel.State.IDLE);
        turret = new Turret();
        turret.setState(Turret.State.RED);
        odo = new Odo();
        waitForStart();
        while (opModeIsActive()){
            flywheel.update();
            turret.update();
            odo.update();
            shooterCalculator.update();
            Hardware.ssh1.setPosition(pos);
            Hardware.mch0.setPower(power);
            Hardware.mch1.setPower(power);
            telemetry.addData("Current Velocity",flywheel.getVelocity());
            telemetry.addData("Target Velocity",flywheel.getTargetVelocity());
            telemetry.addData("Distance", ShooterCalculator.getDistance());
            telemetry.addData("X",Odo.getX());
            telemetry.addData("Y",Odo.getX());
            telemetry.addData("H",Odo.getHeading());
            telemetry.update();

        }
    }
}
