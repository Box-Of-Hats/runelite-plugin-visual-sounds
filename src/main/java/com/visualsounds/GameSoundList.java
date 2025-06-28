package com.visualsounds;

import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class GameSoundList {
    private final Deque<GameSound> gameSounds = new ArrayDeque<>();

    @Setter
    private int maxLength;

    /**
     * Contains the unique sound IDs within {@link #gameSounds} if deduplication should be applied
     */
    private final Set<Integer> dedupe;

    public GameSoundList(int maxLength, boolean dedupe) {
        this.maxLength = maxLength;
        if (dedupe) {
            this.dedupe = new HashSet<>();
        } else {
            this.dedupe = null;
        }
    }

    public void clear() {
        this.gameSounds.clear();
        if (this.dedupe != null) {
            this.dedupe.clear();
        }
    }

    public void add(GameSound gameSound) {
        if (dedupe != null && dedupe.contains(gameSound.soundId)) {
            return;
        }

        this.gameSounds.offerFirst(gameSound);

        while (this.gameSounds.size() > maxLength) {
            GameSound evicted = this.gameSounds.removeLast();
            if (dedupe != null) {
                dedupe.remove(evicted.soundId);
            }
        }
    }

    public Collection<GameSound> getGameSounds() {
        return Collections.unmodifiableCollection(this.gameSounds);
    }
}
