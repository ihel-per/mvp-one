package com.imvp.demo.service.command;

import com.imvp.demo.service.command.InstaPost.PostType;
import org.springframework.stereotype.Service;

@Service
public class CommandDispatcher {

    private final CommandFactory commandFactory;

    public CommandDispatcher(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }


    public InstCommand dispatch(InstaPost instaPost) {

        InstCommand command = null;

        if (instaPost.getType().equals(PostType.STORY)) {
            switch (instaPost.getMimeType()) {
                case PHOTO:
                    command = commandFactory.postStoryImgCommand(instaPost.getItemId());
                    break;
                case VIDEO:
                    throw new IllegalArgumentException();
                default:
                    throw new IllegalArgumentException();
            }
        } else if (instaPost.getType().equals(PostType.ITEM)) {
            switch (instaPost.getMimeType()) {
                case PHOTO:
                    command = commandFactory.publishPostImgCommand(instaPost.getItemId());
                    break;
                case VIDEO:
                    command = commandFactory.publishPostVideoCommand(instaPost.getItemId());
                    break;
                default:
                    throw new IllegalArgumentException();
            }

        }

        return command;

    }

}
