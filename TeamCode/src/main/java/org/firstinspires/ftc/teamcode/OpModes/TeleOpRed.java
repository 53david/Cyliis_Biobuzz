package org.firstinspires.ftc.teamcode.OpModes;

import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.gm1;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Components.Intake.Intake;
import org.firstinspires.ftc.teamcode.Components.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@TeleOp
public class TeleOpRed extends LinearOpMode {
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
        shooter = new Shooter(Shooter.State.ACTIVE);
        intake = new Intake();
        waitForStart();
        while (opModeIsActive()){
            chassis.update();
            odo.update();
            intake.update();
            shooter.update();

            double heading = -Odo.getHeading() - Math.PI/2;
            double X = gm1.left_stick_x;
            double Y = -gm1.left_stick_y;
            double rx = (gm1.right_trigger - gm1.left_trigger);
            double x = X * Math.cos(heading) - Y * Math.sin(heading);
            double y = X * Math.sin(heading) + Y * Math.cos(heading);
            chassis.setTargetVector(x, y, rx);

            if (gamepad1.left_bumper && intake.getState()!= Intake.State.SHOOT) intake.setState(Intake.State.REVERSE);
            else if (gamepad1.right_bumper && intake.getState()!= Intake.State.SHOOT) intake.setState(Intake.State.INTAKE);
            else if (gamepad1.bWasPressed() && intake.getState()!= Intake.State.SHOOT) intake.setState(Intake.State.INTAKE_FROM_FLOWER);
            else if (gamepad1.aWasPressed() && intake.getState()!= Intake.State.SHOOT){ intake.setState(Intake.State.SHOOT); shooter.setState(Shooter.State.SHOOT);}
            else if (intake.getState()!= Intake.State.SHOOT) intake.setState(Intake.State.IDLE);

            telemetry.addData("X",Odo.getX());
            telemetry.addData("Y",Odo.getY());
            telemetry.addData("H",Odo.getHeading());
            telemetry.addData("Velocity",shooter.flyWheel.getVelocity());
            telemetry.update();

        }
    }
}
