package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config
@TeleOp
public class HubTest extends LinearOpMode {
    enum HARDWARE{
        SERVO("ssh"), MOTOR("mch"), SPARKMINI("sch");

        String name;

        HARDWARE(String name)
        {
            this.name=name;
        }
    }
    public static HARDWARE hardware = HARDWARE.MOTOR;

    enum PORT{
        _0("0"), _1("1"), _2("2"), _3("3"), _4("4"), _5("5");

        String number;

        PORT(String number)
        {
            this.number=number;
        }
    }
    public static PORT port=PORT._0;

    public static double power=0,position=0;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        DcMotorEx motor;
        ServoImplEx servo;
        CRServo crServo;

        waitForStart();

        while(opModeIsActive())
        {
            if ((port == PORT._4 || port == PORT._5) && hardware == HARDWARE.MOTOR) port = PORT._3;

            String string = hardware.name+port.number;

            switch (hardware)
            {
                case SPARKMINI:
                    crServo=hardwareMap.get(CRServo.class, string);
                    crServo.setPower(power);
                    break;
                case SERVO:
                    servo=hardwareMap.get(ServoImplEx.class, string);
                    servo.setPwmRange(new PwmControl.PwmRange(500,2500));
                    servo.setPosition(position);
                    telemetry.addData("pos",servo.getPosition());
                    break;
                case MOTOR:
                    motor=hardwareMap.get(DcMotorEx.class, string);
                    motor.setPower(power);
                    telemetry.addData("vel" , motor.getVelocity());
                    telemetry.addData("current" , motor.getCurrent(CurrentUnit.AMPS));
                    break;
            }

            telemetry.update();

        }
    }
}
