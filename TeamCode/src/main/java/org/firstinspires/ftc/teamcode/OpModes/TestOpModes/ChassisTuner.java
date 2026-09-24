package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;
@Config
@TeleOp
public class ChassisTuner extends LinearOpMode {
    public static double x = 0,y = 0,heading = 0;
    public static boolean usePredictiveBreaking = true;
    Odo odo;
    Chassis chassis;
    @Override
    public void runOpMode(){
        Hardware.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        chassis = new Chassis(Chassis.State.PID);
        odo = new Odo();
        waitForStart();
        while (opModeIsActive()){
            chassis.setTargetPosition(x,y,heading,usePredictiveBreaking);
            chassis.update();
            odo.update();
            telemetry.addData("X",Odo.predictedX);
            telemetry.addData("Y",Odo.predictedY);
            telemetry.addData("Heading",Odo.heading);
        }
    }
}
