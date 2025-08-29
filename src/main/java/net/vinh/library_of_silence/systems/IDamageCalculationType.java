package net.vinh.library_of_silence.systems;

@FunctionalInterface
public interface IDamageCalculationType {
    void applyWithCustomLogic(IDamageContext ctx);
}
