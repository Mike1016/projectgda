package com.daka.listener;

import com.daka.service.dictionaries.DictionariesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DictionariesService dictionariesService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            dictionariesService.init();
        }
    }
}
