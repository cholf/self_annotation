package com.bbking.prccessor;

import com.bbking.prccessor.annotation.FConfig;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 *
 * @author gangwen.xu
 * Date  : 2018/5/8
 * Time  : 下午6:09
 * 类描述 :
 */
@AutoService(Process.class)
@SupportedAnnotationTypes("*")
public class SelfAnnotationProccessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(FConfig.class)) {


        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
