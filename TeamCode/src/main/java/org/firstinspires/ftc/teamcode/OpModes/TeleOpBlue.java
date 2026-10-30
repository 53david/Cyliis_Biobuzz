package org.firstinspires.ftc.teamcode.OpModes;

import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.gm1;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.gm2;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.prevgm1;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.prevgm2;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Components.Intake.Ramp;
import org.firstinspires.ftc.teamcode.Components.Intake.Intake;
import org.firstinspires.ftc.teamcode.Components.Intake.Pitch;
import org.firstinspires.ftc.teamcode.Components.Intake.Storage;
import org.firstinspires.ftc.teamcode.Components.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Math.ShooterCalculator;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@TeleOp
public class TeleOpBlue extends LinearOpMode {
    Chassis chassis;
    Intake intake;
    Odo odo;
    Shooter shooter;
    @Override
    public void runOpMode(){

        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        chassis = new Chassis(Chassis.State.DRIVE);
        odo = new Odo();
        shooter = new Shooter(Shooter.State.IDLE);
        shooter.turret.setState(Turret.State.BLUE);
        shooter.turret.pause = false;
        intake = new Intake();
        waitForStart();
        while (opModeIsActive()){
            gm1.copy(gamepad1);
            gm2.copy(gamepad2);

            odo.update();
            if  (gm1.psWasPressed())odo.reset();
            double heading = -Odo.getHeading() + Math.PI/2;
            double X = gm1.left_stick_x;
            double Y = -gm1.left_stick_y;
            double x = X * Math.cos(heading) - Y * Math.sin(heading);
            double y = X * Math.sin(heading) + Y * Math.cos(heading);
            double rx = (gm1.left_trigger - gm1.right_trigger);
            chassis.setTargetVector(x, y, rx);
            chassis.update();
            shooter.update();
                if (gamepad1.left_bumper && intake.getState() != Intake.State.SHOOT)
                    intake.setState(Intake.State.REVERSE);
                else if (gamepad1.right_bumper && intake.getState() != Intake.State.SHOOT)
                    intake.setState(Intake.State.INTAKE);
                else if (gamepad1.a) {
                    intake.setState(Intake.State.SHOOT);
                    shooter.setState(Shooter.State.SHOOT);
                }
                else if (intake.getState()!= Intake.State.SHOOT){intake.setState(Intake.State.IDLE);shooter.setState(Shooter.State.ACTIVE);}
            if (gm1.circle && gm1.circle != prevgm1.circle && (intake.ramp.getState() == Ramp.State.GOING_DOWN || intake.ramp.getState() == Ramp.State.DOWN))intake.retractRamp();
            else if (gm1.circle && gm1.circle != prevgm1.circle && (intake.ramp.getState() == Ramp.State.GOING_UP || intake.ramp.getState() == Ramp.State.UP))intake.deployRamp();

            intake.update();
            prevgm1.copy(gm1);
            prevgm2.copy(gm2);
            telemetry.addData("X",Odo.getX());
            telemetry.addData("Y",Odo.getY());
            telemetry.addData("Heading",Odo.getHeading());
            telemetry.addData("Current Velocity",shooter.flyWheel.getVelocity());
            telemetry.addData("Target Velocity",shooter.flyWheel.getTargetVelocity());
            telemetry.addData("Distance", ShooterCalculator.getDistance());
            telemetry.update();

        }
    }
}
