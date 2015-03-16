# Introdução #

Dentre os itens sugeridos para futuras iterações do código, tem-se a implementação de novas refatorações, de modo a deixar o plugin mais completo para usuários iniciante. A idéia é que o plugin se torne mais útil a programadores que desejem utilizá-lo para a realização das refatorações mais comuns, sem necessidade de estender o plugin ou mesmo modificar o projeto.

# Primeiros Passos #

Nesse caso, as modificações serão realizadas no próprio código do plug-in, que se encontra disponível no repositório de projetos do Google, no endereço http://xmlrefactoring.googlecode.com/svn/trunk. Para começar, deve-se fazer o dowload do código utilizando algum cliente SVN. Em seguida, procede-se à edição do código, utilizando alguma versão do Eclipse com suporte a desenvolvimento de plug-ins.

# Classes a serem estendidas #

Para que uma nova refatoração seja inserida, devem-se estender as seguintes classes:

  * BaseRefactoringArguments
  * BaseProcessor
  * BaseXSDParticipant
  * BaseXLSParticipant
  * XMLRefactoring
  * SingleInputAction OU MultipleInputAction
  * BaseRefactoringWizard
  * BaseUserInputWizardPage

## a). BaseRefactoringArguments ##

Essa classe é responsável por empacotar os argumentos que serão fornecidos à refatoração. Seu construtor deve ser implementado e tem a seguinte forma:

```
public MyActionRefactoringArguments(List<XSDNamedComponents> components)
{
    super(components);
    // código de tratamento dos argumentos
}
```

Repare que o construtor recebe uma lista de componentes. Mesmo que apenas um componente deva ser fornecido como argumento, ele deverá ser inserido em uma lista e, dentro do código de tratamento, bastará acessá-lo por meio do seguinte código:

> `getElements().get(0);`

A função getElements é implementada por BaseRefactoringArguments e retorna a lista de elementos fornecida como argumento ao construtor.

## b). BaseProcessor ##

Essa classe abstrata lida com o processamento dos argumentos recebidos por do plug-in e, também, com a ligação entre a refatoração e os seus respectivos “extension points”. O construtor deve ser implementado:

```
// A propriedade ‘arguments’ deve ser criada
private MyActionRefactoringArguments arguments;

public MyActionProcessor(XSDNamedComponent component)
{
    List<XSDNamedComponent> list = new ArrayList<XSDNamedComponent>();
    list.add(component);
    arguments = new MyActionArguments(list);
}

// OU

public MyActionProcessor(List<XSDNamedComponent> components)
{
    // Código para tratamento de components
    arguments = new MyActionArguments(components);
}
```

Algumas funções devem ser sobrescritas:

```
protected Object getElement()
{
    // Para argument único
    return arguments.getComponents().get(0); 

    // OU Para múltiplos argumentos
    return arguments.getComponents();
}

public String getIdentifier()
{
    return XMLRefactoringMessages.getString(“MyActionParticipants.Identifier”);
}

public String getProcessorName()
{
    return XMLRefactoringMessages.getString(“MyActionParticipants.Name”);
}
```

E outras funções declaradas como abstract na super-classe devem ser implementadas:

```
protected BaseRefactoringArguments getRefactoringArguments()
{
    // Código de tratamento
    return arguments;
}

protected String getParticipantExtensionPoint()
{
    return XMLRefactoringMessages.getString(“MyActionParticipants.ExtensionPointID”);
}
```

## c). BaseXSDParticipant ##

Essa classe é responsável por manter a coerência e executar as modificações no XSD. Para tanto, ao estender essa classe é necessário sobrescrever alguns de seus métodos, principalmente createChange, que executa as mudanças no XSD. Também, é necessário sobrescrever getName e initialize, para manter a coerência do plug-in.

Além disso, no caso de condições especiais serem necessárias para que a mudança no XSD seja executada, é necessário sobrescrever checkConditions.

```
public Change createChange(IprogressMonitor pm) throws CoreException, OperationCancelledException
{
    // Código que realiza mudança no XSD
}

public void initialize(RefactoringArguments arguments)
{
    super.initialize(arguments);
    this.arguments = (MyActionRefactoringArguments) arguments;
}

public String getName()
{
    return XMLRefactoringMessages.getString(“XSDMyActionParticipant.Name”);
}
```

## d). BaseXLSParticipant ##

Essa classe é responsável por criar os arquivos XLST relacionados à refatoração. Ao estender essa classe, é necessário sobrescrever getName, initialize e getXMLRefactoring. Além disso, no caso de condições especiais serem necessárias para que a mudança no XML seja executada, é necessário sobrescrever checkConditions.

```
public void initialize(RefactoringArguments arguments)
{
    super.initialize(arguments);
    this.arguments = (MyActionRefactoringArguments) arguments;
    element = this.arguments.getElements();
    // OU
    element = this.arguments.getElements().get(0);
}

public String getName()
{
    return XMLRefactoringMessages.getString(“XLSTMyActionParticipant.Name”);
}

protected XMLRefactoring getXMLRefactoring() throws CoreException
{
    // Código que cria mudanças a serem executadas no XML
}
```

## e). XMLRefactoring ##

Classe responsável por executar a refatoração. Ao ser estendida, devem ser expostos os construtores reeferente à refatoração e, também, deve-se criar a refatoração inversa, de modo a permitir que a ação seja desfeita.

```
private static String MYACTIONTEMPLATE = “<path>/myAction.vm”;

public MyActionRefactoring(List<List<Qname>> paths, // outros parâmetros pertinentes)
{
    // Código da refatoração
}

public void createReverseRefactoring()
{
    // Refatoração inversa
}
```

## f). SingleInputAction OU MultipleInputAction ##

Essa classe captura a interação do usuário com a interface. Ou seja, ela recolhe os elementos selecionados pelo usuário e sob os quais será executada a refatoração. No caso em que um elemento será selecionado, utiliza-se SingleInputAction. Para múltiplos elementos, utiliza-se MultipleInputAction.

A função getWizard deve ser implementada, de modo a recolher a seleção feita na tela e criar o Wizard que irá executar a ação:

```
protected BaseRefactoringWizard<MyActionProcessor> getWizard()
{
    MyActionProcessor processor = new MyActionProcessor(getSelectedComponents());
    return new MyActionWizard(processor));
}
```

## g). BaseRefactoringWizard ##

Essa classe instancia a interface e agrega o BaseProcessor gerenciar a ação. Para tanto, é necessário sobrescrever o construtor e a função addUserInputPages.

```
private MyActionWizardPage page;

public MyActionWizard(MyActionProcessor processor)
{
    super(processor, DIALOG_BASED_USER_INTERFACE);
}

protected void addUserInputPages()
{
    page = new MyActionWizardPage(this);
    addPage(page);
}
```

## h). BaseRefactoringWizardPage ##

Essa classe representa a página que será apresentada ao Usuário para configuração e confirmação da refatoração a ser executada. Para tanto, deve-se estender o construtor e a função createControl.

```
private MyActionWizard wizard;
private static final String pageName = “My Action Wizard Page”;

public MyActionWizardPage(MyActionWizard wizard)
{
    super(pageName);
    this.wizard = wizard;
}

public void createControl(Composite parent)
{
    // Código que descreve a página
}
```