package org.firstinspires.ftc.teamcode.OpModes;

import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.Voltage;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.gm1;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.gm2;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.k;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.prevgm1;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.prevgm2;
import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.x;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;

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

@TeleOp(group = "TeleOp")
public class TeleOpBlue extends LinearOpMode {
    boolean ok = true;
    VoltageSensor voltageSensor;
    Chassis chassis;
    Intake intake;
    Odo odo;
    Shooter shooter;
    @Override
    public void runOpMode(){

        Hardware.init(hardwareMap);
        voltageSensor = hardwareMap.voltageSensor.iterator().next();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        chassis = new Chassis(Chassis.State.DRIVE);
        odo = new Odo();
        shooter = new Shooter(Shooter.State.IDLE);
        shooter.turret.setState(Turret.State.RED);
        shooter.turret.pause = false;
        intake = new Intake();
        waitForStart();
        while (opModeIsActive()){

            double currentVoltage = voltageSensor.getVoltage();
            if (currentVoltage<=13) Voltage = x/ currentVoltage * k;
            else Voltage = 1;

            if (gm1.right_stick_x>0.65 && prevgm1.right_stick_x<0.65) Odo.offsetX +=10;
            if (gm1.right_stick_x<-0.65 && prevgm1.right_stick_x>-0.65) Odo.offsetX -=10;
            if (gm1.right_stick_y>0.65 && prevgm1.right_stick_y<0.65) Odo.offsetY -=10;
            if (gm1.right_stick_y<-0.65 && prevgm1.right_stick_y>-0.65) Odo.offsetY +=10;

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
            else if (gamepad1.right_bumper && intake.storage.getState()== Storage.State.READY_FOR_TRANSFER) intake.setState(Intake.State.REVERSE);
            else if (gamepad1.right_bumper && intake.getState() != Intake.State.SHOOT) intake.setState(Intake.State.INTAKE);
            else if (gamepad1.a) {
                intake.setState(Intake.State.SHOOT);
                shooter.setState(Shooter.State.SHOOT);
            }
            else if (intake.getState()!= Intake.State.SHOOT){intake.setState(Intake.State.IDLE);shooter.setState(Shooter.State.ACTIVE);}
            if (gm1.circle && gm1.circle != prevgm1.circle && (intake.ramp.getState() == Ramp.State.GOING_DOWN || intake.ramp.getState() == Ramp.State.DOWN))intake.retractRamp();
            else if (gm1.circle && gm1.circle != prevgm1.circle && (intake.ramp.getState() == Ramp.State.GOING_UP || intake.ramp.getState() == Ramp.State.UP))intake.deployRamp();

            if (intake.storage.getState() == Storage.State.READY_FOR_TRANSFER && ok) {
                ok = false; gamepad1.rumble(500);
            }
            else if (intake.storage.getState() == Storage.State.NO_POLLEN)ok = true;
            intake.update();
            prevgm1.copy(gm1);
            prevgm2.copy(gm2);
            telemetry.addData("X",Odo.getX());
            telemetry.addData("Y",Odo.getY());
            telemetry.addData("Heading",Odo.getHeading());
            telemetry.addData("Current Velocity",shooter.flyWheel.getVelocity());
            telemetry.addData("Target Velocity",shooter.flyWheel.getTargetVelocity());
            telemetry.addData("Distance", ShooterCalculator.getDistance());
            telemetry.addData("Storage",intake.storage.getState());
            telemetry.update();

        }
    }
}
