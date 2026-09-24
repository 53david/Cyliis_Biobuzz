package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Config
@TeleOp
public class TurretTest extends LinearOpMode {
    Turret turret;
    Odo odo;
    public static double x;
    @Override
    public void runOpMode() throws InterruptedException{
        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        turret = new Turret();
        odo = new Odo();
        turret.setState(Turret.State.BLUE);
        while (opModeIsActive()){
            if (x == 0) turret.setState(Turret.State.BLUE);
            if (x == 1) turret.setState(Turret.State.RED);
            turret.update();
            odo.update();
            telemetry.addData("State",turret.getState());
            telemetry.addData("State",turret.getHiveState());
            telemetry.addData("X",Odo.predictedX);
            telemetry.addData("Y",Odo.predictedY);
            telemetry.addData("Heading",Odo.heading);
            telemetry.update();

        }
    }
}
