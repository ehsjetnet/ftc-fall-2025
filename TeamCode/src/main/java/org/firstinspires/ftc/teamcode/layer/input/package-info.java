/**
 * Layers concerned with the capture and processing of input.
 * Directly contained in this package are {@link AbstractInputGenerator input generators},
 * perpetually incomplete layers that return a task containing a snapshot of input gathered from
 * external sources whenever updated.
 *
 * <p>Input generators do not necessarily need to emit <i>live</i> user input. One could be written
 * that replays input from a file for testing, for example.
 */
package org.firstinspires.ftc.teamcode.layer.input;
