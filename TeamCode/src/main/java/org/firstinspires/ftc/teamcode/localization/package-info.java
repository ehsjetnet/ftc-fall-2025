/**
 * Classes for determining and keeping track of the robot's field space transform.
 * "Localization," also called odometry, is the process of figuring out (typically from sensory
 * input but also from other heuristics) the robot's position and rotation on the field. The
 * localization system runs outside of any layer because it has potential applications at every
 * level. {@link LocalizationData} is collected from multiple {@link LocalizationSource}s, then
 * combined into a single best-guess transform by a {@link RobotLocalizer}, which also handles
 * distributing this information to layers that need it.
 *
 * <p>All localization data is uncertain, though data collected from some sources is more reliable
 * than others. This is measured in two parameters: accuracy and precision. Accuracy is a measure of
 * how trustworthy the data is; known interference lowers this value. Precision is a measure of how
 * much more likely the robot is to actually be in the datum's guessed state rather than neighboring
 * states. This value is a property of the measurement method and instruments used. The uncertainty
 * from the variation of both these values is accounted for by expressing each localization datum
 * not as a discrete value for the best-guess position or rotation but as a probablility according
 * to the datum of the robot being in a given state. Picking a most likely state for the robot is
 * then the task of finding a transform that maximizes the sum of the probabilities across all
 * localization data collected in an update.
 */
package org.firstinspires.ftc.teamcode.localization;
