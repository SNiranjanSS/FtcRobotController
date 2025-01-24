/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * This OpMode scans a single servo back and forward until Stop is pressed.
 * The code is structured as a LinearOpMode
 * INCREMENT sets how much to increase/decrease the servo position each cycle
 * CYCLE_MS sets the update period.
 *
 * This code assumes a Servo configured with the name "left_hand" as is found on a Robot.
 *
 * NOTE: When any servo position is set, ALL attached servos are activated, so ensure that any other
 * connected servos are able to move freely before running this test.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
@TeleOp(name = "Teleop Mode", group = "Concept")
public class TeleOpMode extends LinearOpMode {

    private MecanumWheels mecanumWheels;    // for mecanumWheels

    DcMotor arm; // motor for arm
    DcMotor slide; // motor for slide
    Servo elbow; // servo for elbow
    Servo claw; // servo for claw

    // for arm and slide
    static final double COUNTS_PER_MOTOR_REV43 = 3895.9; // setting for 43 RPM motor
    static final double COUNTS_PER_MOTOR_REV312 = 3895.9; // setting for 43 RPM motor
    static final int CYCLE_MS = 50; // period of each cycle, set to 50 milliseconds
    int targetPositionArm = 0; // To store the current target position for arm
    int targetPositionSlide = 0; // To store the current position for slide

    // For elbow joint
    static final double INCREMENT_ELBOW = 0.03;     // amount to slew servo each CYCLE_MS cycle
    static double MAX_POS_ELBOW =  1;     // Maximum rotational position
    static double MIN_POS_ELBOW =  0;       // Minimum rotational position
    double positionElbow = 0; // Start at minimum position

    // For claw
    static final double MAX_POS_CLAW     =  0.6;     // Maximum rotational position
    static final double MIN_POS_CLAW     =  0.4;       // Minimum rotational position
    double positionClaw = MIN_POS_CLAW; // Start at min

    @Override
    public void runOpMode() {
        // mecanum wheels program
        mecanumWheels = new MecanumWheels(this);

        // map motors and servos
        arm = hardwareMap.get(DcMotor.class, "AR");
        slide = hardwareMap.get(DcMotor.class, "SL");
        elbow = hardwareMap.get(Servo.class, "EL");
        claw = hardwareMap.get(Servo.class, "CL");

        // Wait for the start button
        telemetry.addData(">", "Press Start to run Teleop.");
        telemetry.update();
        waitForStart();

        // setup arm
        // Set arm direction, mode, and behavior
        arm.setDirection(DcMotor.Direction.REVERSE); // Adjust this as needed
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setTargetPosition(0);

        // Set slide direction, mode, and behavior
        slide.setDirection(DcMotor.Direction.REVERSE); // Adjust this as needed
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setTargetPosition(0);

        // Define the target positions in encoder counts for arm and slide
        final double targetScore = 0.35; // Set appropriately
        final double targetPickUp = 0.6;            // Set appropriately
        final double targetZeroARM = 0;
        final double targetZeroSLIDE = 0;
        //double targetEnter = 0.6;
        final double targetExtend = 0.6;  // Set as needed

        while (opModeIsActive()) {
            // move mecanum wheels
            mecanumWheels.move();

            // Check which button is pressed and set the target position of arm
            if (gamepad2.dpad_up) {
                targetPositionArm = (int) (COUNTS_PER_MOTOR_REV43 * targetScore);
                MAX_POS_ELBOW = 0.6;
                MIN_POS_ELBOW = 0.3;
                positionElbow = MAX_POS_ELBOW;
            } else if (gamepad2.dpad_left) {
                targetPositionArm = (int) (COUNTS_PER_MOTOR_REV43 * targetPickUp);
                MAX_POS_ELBOW = 0.7;
                MIN_POS_ELBOW = 0.5;
                positionElbow = MAX_POS_ELBOW;
            } else if (gamepad2.dpad_right) {
                targetPositionArm = (int) (COUNTS_PER_MOTOR_REV43 * targetZeroARM);
                MAX_POS_ELBOW = 1;
                MIN_POS_ELBOW = 0;
                positionElbow = (MIN_POS_ELBOW + MAX_POS_ELBOW)/2;
            }
//           else if (gamepad2.dpad_right) {
//                targetPositionArm = (int) (COUNTS_PER_MOTOR_REV43 * targetEnter);
//            }

            // moves slide in/out
            if (gamepad1.a && !slide.isBusy()) {
                if (targetPositionSlide == targetZeroSLIDE) {
                    targetPositionSlide = (int) (COUNTS_PER_MOTOR_REV312 * targetExtend);
                } else if (targetPositionSlide == targetExtend){
                    targetPositionSlide = (int) (COUNTS_PER_MOTOR_REV312 * targetZeroSLIDE);
                }
            }
            // Check if the target position has changed
            if (arm.getTargetPosition() != targetPositionArm) {
                arm.setTargetPosition(targetPositionArm);
                arm.setPower(1); // Move to the target position
            }

            // Stop the arm once it has reached the target position
            if (!arm.isBusy()){
                if(targetPositionArm == (int) (COUNTS_PER_MOTOR_REV43 * targetZeroARM)){
                    arm.setPower(0);
                }else {
                    arm.setPower(0.3); // Stop the motor
                }
            }

            // Check if the target position has changed
            if (slide.getTargetPosition() != targetPositionSlide) {
                slide.setTargetPosition(targetPositionSlide);
                slide.setPower(1); // Move to the target position
            }

            // Stop the motor once it has reached the target position
            //change positions so you dont use targetZero for slide and arm
            if (!slide.isBusy()) {
                if(targetPositionSlide == (int) (COUNTS_PER_MOTOR_REV312 * targetZeroSLIDE)){
                    slide.setPower(0);//let the motor rest in its base position
                }else {
                    slide.setPower(0.3); // Stop the motor
                }
            }

            // slew the slide, according to the control.
            if (gamepad2.right_stick_y > 0.5) {
                // Keep stepping up until we hit the max value.
                positionElbow += INCREMENT_ELBOW;
                if (positionElbow >= MAX_POS_ELBOW) {
                    positionElbow = MAX_POS_ELBOW;
                }
            }
            else if (gamepad2.right_stick_y < -0.05){
                // Keep stepping down until we hit the min value.
                positionElbow -= INCREMENT_ELBOW;
                if (positionElbow <= MIN_POS_ELBOW) {
                    positionElbow = MIN_POS_ELBOW;
                }
            }

            // slew the claw, according to the position variable.
            if (gamepad1.b && (claw.getPosition() == positionClaw)) {
                if (positionClaw == MIN_POS_CLAW) {
                    positionClaw = MAX_POS_CLAW;
                } else if (positionClaw == MAX_POS_CLAW) {
                    positionClaw = MIN_POS_CLAW;
                }
            }

            // Set the elbow and claw to the new position and pause;
            elbow.setPosition(positionElbow);
            claw.setPosition(positionClaw);

            sleep(CYCLE_MS); // Pause for 50 milliseconds
            idle();
        }
    }
}