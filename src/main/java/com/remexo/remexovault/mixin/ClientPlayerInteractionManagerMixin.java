package com.remexo.remexovault.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.VaultBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        BlockState state = client.world.getBlockState(hitResult.getBlockPos());

        if (state.getBlock() instanceof VaultBlock) {
            ItemStack heldItem = player.getStackInHand(hand);

            if (heldItem.isOf(Items.OMINOUS_TRIAL_KEY) || heldItem.isOf(Items.TRIAL_KEY)) {
                BlockEntity be = client.world.getBlockEntity(hitResult.getBlockPos());

                if (be instanceof VaultBlockEntity vaultEntity) {
                    ItemStack displayItem = vaultEntity.getSharedData().getDisplayItem();

                    if (!displayItem.isOf(Items.HEAVY_CORE)) {
                        player.sendMessage(
                            Text.literal("§e[Remexovault] §cWait! Heavy Core screen par nahi hai. Key safe hai!"), 
                            true
                        );
                        cir.setReturnValue(ActionResult.FAIL);
                        cir.cancel();
                    } else {
                        player.sendMessage(
                            Text.literal("§e[Remexovault] §aHEAVY CORE DETECTED! Key inserted!"), 
                            true
                        );
                    }
                }
            }
        }
    }
                          }
