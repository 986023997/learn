# spring源码学习

## 1.bean的加载

首先通过XmlBeanFactory加载配置文件构建bean对象，

```
public XmlBeanFactory(Resource resource) throws BeansException {
    this(resource, (BeanFactory)null);
}

public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory) throws BeansException {
    super(parentBeanFactory);
    //封装资源文件
    this.reader = new XmlBeanDefinitionReader(this);
    //加载配置文件
    this.reader.loadBeanDefinitions(resource);
}
```

1.先对资源文件进行封装

2.获得输入流，从Resource中获取对应的InputStream并构造InputSource

3.通过构造的InputSource实例和Resource实例继续调用函数doLoadBeanDefinitions

```
protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource) throws BeanDefinitionStoreException {
    try {
    //解析配置文件
        Document doc = this.doLoadDocument(inputSource, resource);
        //注册bean
        int count = this.registerBeanDefinitions(doc, resource);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Loaded " + count + " bean definitions from " + resource);
        }

        return count;
    } catch (BeanDefinitionStoreException var5) {
        throw var5;
    } catch (SAXParseException var6) {
        throw new XmlBeanDefinitionStoreException(resource.getDescription(), "Line " + var6.getLineNumber() + " in XML document from " + resource + " is invalid", var6);
    } catch (SAXException var7) {
        throw new XmlBeanDefinitionStoreException(resource.getDescription(), "XML document from " + resource + " is invalid", var7);
    } catch (ParserConfigurationException var8) {
        throw new BeanDefinitionStoreException(resource.getDescription(), "Parser configuration exception parsing XML from " + resource, var8);
    } catch (IOException var9) {
        throw new BeanDefinitionStoreException(resource.getDescription(), "IOException parsing XML document from " + resource, var9);
    } catch (Throwable var10) {
        throw new BeanDefinitionStoreException(resource.getDescription(), "Unexpected exception parsing XML document from " + resource, var10);
    }
}
```

跟踪源码最后可以看到进入了DefaultListableBeanFactory的registerBeanDefinition,将bean对象



```
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        Assert.hasText(beanName, "Bean name must not be empty");
        Assert.notNull(beanDefinition, "BeanDefinition must not be null");
        if (beanDefinition instanceof AbstractBeanDefinition) {
            try {
                ((AbstractBeanDefinition)beanDefinition).validate();
            } catch (BeanDefinitionValidationException var8) {
                throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Validation of bean definition failed", var8);
            }
        }

        BeanDefinition existingDefinition = (BeanDefinition)this.beanDefinitionMap.get(beanName);
        if (existingDefinition != null) {
            if (!this.isAllowBeanDefinitionOverriding()) {
                throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
            }

            if (existingDefinition.getRole() < beanDefinition.getRole()) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Overriding user-defined bean definition for bean '" + beanName + "' with a framework-generated bean definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
                }
            } else if (!beanDefinition.equals(existingDefinition)) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Overriding bean definition for bean '" + beanName + "' with a different definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
                }
            } else if (this.logger.isTraceEnabled()) {
                this.logger.trace("Overriding bean definition for bean '" + beanName + "' with an equivalent definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
            }
			//添加到bean容器中，此时bean对象被包装为一个BeanDefinition的对象
            this.beanDefinitionMap.put(beanName, beanDefinition);
        } else {
            if (this.hasBeanCreationStarted()) {
                synchronized(this.beanDefinitionMap) {
                    this.beanDefinitionMap.put(beanName, beanDefinition);
                    List<String> updatedDefinitions = new ArrayList(this.beanDefinitionNames.size() + 1);
                    updatedDefinitions.addAll(this.beanDefinitionNames);
                    updatedDefinitions.add(beanName);
                    this.beanDefinitionNames = updatedDefinitions;
                    this.removeManualSingletonName(beanName);
                }
            } else {
                this.beanDefinitionMap.put(beanName, beanDefinition);
                this.beanDefinitionNames.add(beanName);
                this.removeManualSingletonName(beanName);
            }

            this.frozenBeanDefinitionNames = null;
        }

        if (existingDefinition == null && !this.containsSingleton(beanName)) {
            if (this.isConfigurationFrozen()) {
                this.clearByTypeCache();
            }
        } else {
            this.resetBeanDefinition(beanName);
        }

    }
```

## 2.默认标签的解析

spring中的标签包含了默认标签和自定义标签，两种标签的解析存在着很大不同

对于默认标签的解析是在parseDefaultElement中进行的

```
private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
    if (delegate.nodeNameEquals(ele, "import")) {
        this.importBeanDefinitionResource(ele);
    } else if (delegate.nodeNameEquals(ele, "alias")) {
        this.processAliasRegistration(ele);
    } else if (delegate.nodeNameEquals(ele, "bean")) {
        this.processBeanDefinition(ele, delegate);
    } else if (delegate.nodeNameEquals(ele, "beans")) {
        this.doRegisterBeanDefinitions(ele);
    }
```

### bean的解析与注册

进入到processBeanDefinition

```
protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
//首先委托BeanDefinitionParserDelegate类的parseBeanDefinitionElement对元素进行解析，得到配置文件中的各种属性
    BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
    if (bdHolder != null) {
    //返回的bdHolder且若存在默认标签的子节点下再有自定义属性，还需要再次对自定义标签进行解析
        bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);

        try {
        //解析完成后，对bdHolder进行注册
            BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, this.getReaderContext().getRegistry());
        } catch (BeanDefinitionStoreException var5) {
            this.getReaderContext().error("Failed to register bean definition with name '" + bdHolder.getBeanName() + "'", ele, var5);
        }
		//发出响应事件，通知相关的监听器，这个bean已经加载完成
        this.getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
    }

}
```



```
BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
```

#### 解析BeanDefinition

进入到BeanDefinitionParserDelegate类的parseBeanDefinitionElement对元素进行解析

```
@Nullable
public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, @Nullable BeanDefinition containingBean) {
	//解析id属性
    String id = ele.getAttribute("id");
    //解析name属性
    String nameAttr = ele.getAttribute("name");
    //分割name属性
    List<String> aliases = new ArrayList();
    if (StringUtils.hasLength(nameAttr)) {
        String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, ",; ");
        aliases.addAll(Arrays.asList(nameArr));
    }

    String beanName = id;
    if (!StringUtils.hasText(id) && !aliases.isEmpty()) {
        beanName = (String)aliases.remove(0);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("No XML 'id' specified - using '" + beanName + "' as bean name and " + aliases + " as aliases");
        }
    }

    if (containingBean == null) {
        this.checkNameUniqueness(beanName, aliases, ele);
    }
	//解析其他属性
    AbstractBeanDefinition beanDefinition = this.parseBeanDefinitionElement(ele, beanName, containingBean);
    if (beanDefinition != null) {
        if (!StringUtils.hasText(beanName)) {
            try {
                if (containingBean != null) {
                    beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, this.readerContext.getRegistry(), true);
                } else {
                    beanName = this.readerContext.generateBeanName(beanDefinition);
                    String beanClassName = beanDefinition.getBeanClassName();
                    if (beanClassName != null && beanName.startsWith(beanClassName) && beanName.length() > beanClassName.length() && !this.readerContext.getRegistry().isBeanNameInUse(beanClassName)) {
                        aliases.add(beanClassName);
                    }
                }

                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Neither XML 'id' nor 'name' specified - using generated bean name [" + beanName + "]");
                }
            } catch (Exception var9) {
                this.error(var9.getMessage(), ele);
                return null;
            }
        }

        String[] aliasesArray = StringUtils.toStringArray(aliases);
        return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
    } else {
        return null;
    }
}
```

这里主要工作：

1.提取元素中的id和name属性

2.进一步解析其他属性

3.如果监测到bean没有指定的beanName,那么使用默认规则会为此bean生成beanName;

4.将获取的信息封装到BeanDefinitionHolder类中



进一步进入parseBeanDefinitionElement，查看对其他标签的解析过程

```
@Nullable
public AbstractBeanDefinition parseBeanDefinitionElement(Element ele, String beanName, @Nullable BeanDefinition containingBean) {
    this.parseState.push(new BeanEntry(beanName));
    String className = null;
    //解析class属性
    if (ele.hasAttribute("class")) {
        className = ele.getAttribute("class").trim();
    }
	//解析parent属性
    String parent = null;
    if (ele.hasAttribute("parent")) {
        parent = ele.getAttribute("parent");
    }

    try {
    //创建用于承载属性的AbstractBeanDefinition类型的GenericBeanDefinition
        AbstractBeanDefinition bd = this.createBeanDefinition(className, parent);
        //硬编码解析默认bean的各种属性
        this.parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
        //提取description
        bd.setDescription(DomUtils.getChildElementValueByTagName(ele, "description"));
        //解析元数据
        this.parseMetaElements(ele, bd);
        //解析lockup-method属性
        this.parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
        //解析replaced-method属性
        this.parseReplacedMethodSubElements(ele, bd.getMethodOverrides());
        //解析构造函数参数
        this.parseConstructorArgElements(ele, bd);
        //解析property子元素
        this.parsePropertyElements(ele, bd);
        //解析qualifier子元素
        this.parseQualifierElements(ele, bd);
        bd.setResource(this.readerContext.getResource());
        bd.setSource(this.extractSource(ele));
        AbstractBeanDefinition var7 = bd;
        return var7;
    } catch (ClassNotFoundException var13) {
        this.error("Bean class [" + className + "] not found", ele, var13);
    } catch (NoClassDefFoundError var14) {
        this.error("Class that bean class [" + className + "] depends on not found", ele, var14);
    } catch (Throwable var15) {
        this.error("Unexpected failure during bean definition parsing", ele, var15);
    } finally {
        this.parseState.pop();
    }

    return null;
}
```