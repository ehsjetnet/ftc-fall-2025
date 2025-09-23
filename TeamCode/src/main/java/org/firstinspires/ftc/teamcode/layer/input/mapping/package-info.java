/**
 * Layers that process raw input tasks and translate them into robot commands.
 * These layers are best compared to keybinds in video games. They dictate what controls send what
 * tasks in teleop mode.
 *
 * <p>All input mappings are {@link org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer}s because they
 * should translate raw input tasks to game actions one-to-one. If a mapping needs to emit more than
 * one kind of task, it is best split into two mappings which are multiplexed in the opmode layer
 * list so they can be changed independently. Exceptions might be made for mappings that the
 * author is absolutely sure only make sense in combination.
 */
package org.firstinspires.ftc.teamcode.layer.input.mapping;
