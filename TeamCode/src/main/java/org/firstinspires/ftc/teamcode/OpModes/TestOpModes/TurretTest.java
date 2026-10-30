package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Math.ShooterCalculator;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Config
@TeleOp
public class TurretTest extends LinearOpMode {
    public static double x = 0;
    Turret turret;
    Odo odo;
    @Override
    public void runOpMode() throws InterruptedException{
        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        turret = new Turret();
        odo = new Odo();
        waitForStart();
        while (opModeIsActive()){

            double a = x * Math.PI/180;
            a %= (2.0 * Math.PI);
            if (a < 0) a += (2.0 * Math.PI);
            if (a > Math.PI) a = a - 2 * Math.PI;

            double p = 0.5 + (a * 180/Math.PI) * 36/80 * 2/355;
            p = Math.min(0.96, p);
            p = Math.max(0.046, p);


            turret.update();
            odo.update();

            telemetry.addData("X",Odo.getX());
            telemetry.addData("Y",Odo.getY());
            telemetry.addData("H",Odo.getHeading());
            telemetry.addData("pos",turret.targetPosition);
            telemetry.addData("angle",turret.rAngle);
            telemetry.addData("x", ShooterCalculator.x);
            telemetry.addData("y", ShooterCalculator.y);
            telemetry.update();

        }
    }
}
