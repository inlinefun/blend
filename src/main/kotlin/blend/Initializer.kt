package blend

import net.fabricmc.api.ClientModInitializer

class Initializer: ClientModInitializer {
    override fun onInitializeClient() {
        Client.init()
    }
}