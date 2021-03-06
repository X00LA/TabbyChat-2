package mnm.mods.tabbychat.extra.filters;

import com.google.common.eventbus.Subscribe;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent;
import mnm.mods.tabbychat.extra.filters.ChannelFilter.ChannelAction;
import mnm.mods.tabbychat.extra.filters.ChatFilter.DefaultAction;
import mnm.mods.tabbychat.extra.filters.MessageFilter.MessageAction;
import mnm.mods.tabbychat.settings.ServerSettings;

public class FilterAddon {

    private ChatFilter channelFilter = new ChannelFilter();
    private ChatFilter messageFilter = new MessageFilter();

    public FilterAddon() {
        AddonManager addons = TabbyAPI.getAPI().getAddonManager();
        addons.registerFilterAction(MessageAction.ID, new MessageAction());
        addons.registerFilterAction(ChannelAction.ID, new ChannelAction());
        addons.registerFilterAction(DefaultAction.ID, new DefaultAction());
    }

    @Subscribe
    public void onChatRecieved(ChatReceivedEvent message) {
        ServerSettings settings = TabbyChat.getInstance().serverSettings;
        if (settings == null) {
            // We're possibly not in game.
            return;
        }

        channelFilter.applyFilter(message);
        messageFilter.applyFilter(message);
        for (ChatFilter filter : settings.filters) {
            filter.applyFilter(message);
        }
    }
}
