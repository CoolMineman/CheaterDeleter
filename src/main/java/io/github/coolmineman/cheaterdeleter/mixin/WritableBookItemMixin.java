package io.github.coolmineman.cheaterdeleter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.item.WritableBookItem;

@Mixin(WritableBookItem.class)
public class WritableBookItemMixin {
    @ModifyConstant(method = "isValid", constant = @Constant(intValue = 32767))
    private static int maxPageSize(int bad) {
        return 2000;
    }
}
