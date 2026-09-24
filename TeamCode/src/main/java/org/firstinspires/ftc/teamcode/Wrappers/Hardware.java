package org.firstinspires.ftc.teamcode.Wrappers;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class Hardware {
    public static Gamepad gm1,gm2,prevgm1,prevgm2;
    public static DcMotorEx mch0, mch1, mch2, mch3;
    public static Limelight3A limelight;
    public static ServoImplEx ssh0, ssh1, ssh2, ssh3, ssh4, ssh5;
    public static CRServo sch0, sch1, sch2, sch3;
    public static GoBildaPinpointDriver pp;
    public static DigitalChannel bb1,bb2,bb3,bb4;

    public static void unlock(DcMotorEx motor)
    {
        MotorConfigurationType mct = motor.getMotorType();
        mct.setAchieveableMaxRPMFraction(1);
        motor.setMotorType(mct);
    }



    public static void init(HardwareMap hardwareMap)
    {

        gm1 = new Gamepad();
        prevgm1 = new Gamepad();
        gm2 = new Gamepad();
        prevgm2 = new Gamepad();

        mch0=hardwareMap.get(DcMotorEx.class, "mch0");
        mch1=hardwareMap.get(DcMotorEx.class, "mch1");
        mch2=hardwareMap.get(DcMotorEx.class, "mch2");
        mch3=hardwareMap.get(DcMotorEx.class, "mch3");

        unlock(mch0);
        unlock(mch1);
        unlock(mch2);
        unlock(mch3);

        ssh0=hardwareMap.get(ServoImplEx.class, "ssh0");
        ssh1=hardwareMap.get(ServoImplEx.class, "ssh1");
        ssh2=hardwareMap.get(ServoImplEx.class, "ssh2");
        ssh3=hardwareMap.get(ServoImplEx.class, "ssh3");
        ssh4=hardwareMap.get(ServoImplEx.class, "ssh4");
        ssh5=hardwareMap.get(ServoImplEx.class, "ssh5");


        sch0=hardwareMap.get(CRServo.class, "sch0");
        sch1=hardwareMap.get(CRServo.class, "sch1");
        sch2=hardwareMap.get(CRServo.class, "sch2");
        sch3=hardwareMap.get(CRServo.class, "sch3");


        bb1=hardwareMap.get(DigitalChannel.class,"bb1");
        bb2=hardwareMap.get(DigitalChannel.class,"bb2");
        bb3=hardwareMap.get(DigitalChannel.class,"bb3");
        bb4=hardwareMap.get(DigitalChannel.class,"bb4");


        pp = hardwareMap.get(GoBildaPinpointDriver.class,"odo");


    }

}